package io.github.akhil0072.search;

import java.text.Normalizer;
import java.util.ArrayList;
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

    private final Map<String, Document> documentsById;
    private final Map<String, Set<String>> postingsByTerm;
    private final IndexStatistics statistics;

    private SearchEngine(Map<String, Document> documentsById, Map<String, Set<String>> postingsByTerm) {
        this.documentsById = Map.copyOf(documentsById);
        this.postingsByTerm = Map.copyOf(postingsByTerm);
        int postingCount = postingsByTerm.values().stream().mapToInt(Set::size).sum();
        this.statistics = new IndexStatistics(
                documentsById.size(),
                postingsByTerm.size(),
                postingCount);
    }

    public static SearchEngine from(List<Document> documents) {
        Objects.requireNonNull(documents, "documents");
        Map<String, Document> documentsById = new TreeMap<>();
        Map<String, Set<String>> postingsByTerm = new TreeMap<>();

        for (Document document : documents) {
            Objects.requireNonNull(document, "document");
            if (documentsById.putIfAbsent(document.id(), document) != null) {
                throw new IllegalArgumentException("Duplicate document id: " + document.id());
            }
            for (String term : tokenize(document.content())) {
                postingsByTerm.computeIfAbsent(term, ignored -> new TreeSet<>()).add(document.id());
            }
        }

        return new SearchEngine(documentsById, postingsByTerm);
    }

    public List<Document> search(String query) {
        Objects.requireNonNull(query, "query");
        Set<String> queryTerms = tokenize(query);
        if (queryTerms.isEmpty()) {
            return List.of();
        }

        Set<String> matchingIds = null;
        for (String term : queryTerms) {
            Set<String> postings = postingsByTerm.get(term);
            if (postings == null) {
                return List.of();
            }
            if (matchingIds == null) {
                matchingIds = new TreeSet<>(postings);
            } else {
                matchingIds.retainAll(postings);
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

    public IndexStatistics statistics() {
        return statistics;
    }

    private static Set<String> tokenize(String text) {
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFKC)
                .toLowerCase(Locale.ROOT);
        Set<String> terms = new LinkedHashSet<>();
        for (String term : TOKEN_SEPARATOR.split(normalized)) {
            if (!term.isBlank()) {
                terms.add(term);
            }
        }
        return terms;
    }
}
