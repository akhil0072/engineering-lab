package io.github.akhil0072.search;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;

public final class SearchEngine {
    private static final Pattern TOKEN_SEPARATOR = Pattern.compile("[^\\p{L}\\p{Nd}]+");
    private static final double BM25_K1 = 1.2;
    private static final double BM25_B = 0.75;

    private final Map<String, Document> documentsById;
    private final Map<String, Map<String, Integer>> termFrequenciesByTerm;
    private final Map<String, Integer> documentLengths;
    private final double averageDocumentLength;
    private final IndexStatistics statistics;

    private SearchEngine(
            Map<String, Document> documentsById,
            Map<String, Map<String, Integer>> termFrequenciesByTerm,
            Map<String, Integer> documentLengths) {
        this.documentsById = Map.copyOf(documentsById);
        Map<String, Map<String, Integer>> immutableTermFrequencies = new TreeMap<>();
        termFrequenciesByTerm.forEach(
                (term, frequencies) -> immutableTermFrequencies.put(term, Map.copyOf(frequencies)));
        this.termFrequenciesByTerm = Map.copyOf(immutableTermFrequencies);
        this.documentLengths = Map.copyOf(documentLengths);
        this.averageDocumentLength = documentLengths.values().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
        int postingCount = termFrequenciesByTerm.values().stream().mapToInt(Map::size).sum();
        this.statistics = new IndexStatistics(
                documentsById.size(),
                termFrequenciesByTerm.size(),
                postingCount);
    }

    public static SearchEngine from(List<Document> documents) {
        Objects.requireNonNull(documents, "documents");
        Map<String, Document> documentsById = new TreeMap<>();
        Map<String, Map<String, Integer>> termFrequenciesByTerm = new TreeMap<>();
        Map<String, Integer> documentLengths = new TreeMap<>();

        for (Document document : documents) {
            Objects.requireNonNull(document, "document");
            if (documentsById.putIfAbsent(document.id(), document) != null) {
                throw new IllegalArgumentException("Duplicate document id: " + document.id());
            }
            List<String> terms = tokenize(document.content());
            documentLengths.put(document.id(), terms.size());
            for (String term : terms) {
                termFrequenciesByTerm
                        .computeIfAbsent(term, ignored -> new TreeMap<>())
                        .merge(document.id(), 1, Integer::sum);
            }
        }

        return new SearchEngine(documentsById, termFrequenciesByTerm, documentLengths);
    }

    public List<Document> search(String query) {
        Objects.requireNonNull(query, "query");
        Set<String> queryTerms = new LinkedHashSet<>(tokenize(query));
        if (queryTerms.isEmpty()) {
            return List.of();
        }

        Set<String> matchingIds = null;
        for (String term : queryTerms) {
            Map<String, Integer> postings = termFrequenciesByTerm.get(term);
            if (postings == null) {
                return List.of();
            }
            if (matchingIds == null) {
                matchingIds = new TreeSet<>(postings.keySet());
            } else {
                matchingIds.retainAll(postings.keySet());
            }
            if (matchingIds.isEmpty()) {
                return List.of();
            }
        }

        List<Document> matches = new ArrayList<>(matchingIds.size());
        for (String id : matchingIds) {
            matches.add(documentsById.get(id));
        }
        return List.copyOf(matches);
    }

    public List<SearchResult> rankedSearch(String query, int limit) {
        Objects.requireNonNull(query, "query");
        if (limit < 1) {
            throw new IllegalArgumentException("limit must be positive");
        }

        Set<String> queryTerms = new LinkedHashSet<>(tokenize(query));
        if (queryTerms.isEmpty()) {
            return List.of();
        }

        Map<String, Double> scoresByDocumentId = new TreeMap<>();
        int documentCount = documentsById.size();
        for (String term : queryTerms) {
            Map<String, Integer> frequencies = termFrequenciesByTerm.get(term);
            if (frequencies == null) {
                continue;
            }

            int documentFrequency = frequencies.size();
            double inverseDocumentFrequency = Math.log(
                    1.0 + (documentCount - documentFrequency + 0.5) / (documentFrequency + 0.5));
            for (Map.Entry<String, Integer> frequency : frequencies.entrySet()) {
                String documentId = frequency.getKey();
                double termFrequency = frequency.getValue();
                double lengthRatio = documentLengths.get(documentId) / averageDocumentLength;
                double denominator = termFrequency
                        + BM25_K1 * (1.0 - BM25_B + BM25_B * lengthRatio);
                double termScore = inverseDocumentFrequency
                        * (termFrequency * (BM25_K1 + 1.0))
                        / denominator;
                scoresByDocumentId.merge(documentId, termScore, Double::sum);
            }
        }

        return scoresByDocumentId.entrySet().stream()
                .map(entry -> new SearchResult(documentsById.get(entry.getKey()), entry.getValue()))
                .sorted(Comparator.comparingDouble(SearchResult::score)
                        .reversed()
                        .thenComparing(result -> result.document().id()))
                .limit(limit)
                .toList();
    }

    public IndexStatistics statistics() {
        return statistics;
    }

    private static List<String> tokenize(String text) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFKC)
                .toLowerCase(Locale.ROOT);
        List<String> terms = new ArrayList<>();
        for (String term : TOKEN_SEPARATOR.split(normalized)) {
            if (!term.isBlank()) {
                terms.add(term);
            }
        }
        return terms;
    }
}
