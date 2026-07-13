package io.github.akhil0072.search;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class BaselineRunner {
    private static final int DEFAULT_DOCUMENT_COUNT = 100;
    private static final int DEFAULT_QUERY_ITERATIONS = 10_000;

    private BaselineRunner() {}

    public static void main(String[] args) {
        int documentCount = parsePositiveArgument(args, 0, DEFAULT_DOCUMENT_COUNT);
        int queryIterations = parsePositiveArgument(args, 1, DEFAULT_QUERY_ITERATIONS);
        List<Document> documents = corpus(documentCount);

        long indexStart = System.nanoTime();
        SearchEngine engine = SearchEngine.from(documents);
        long indexElapsed = System.nanoTime() - indexStart;

        for (int iteration = 0; iteration < 1_000; iteration++) {
            engine.search("java search");
        }

        long queryStart = System.nanoTime();
        List<Document> matches = List.of();
        for (int iteration = 0; iteration < queryIterations; iteration++) {
            matches = engine.search("java search");
        }
        long queryElapsed = System.nanoTime() - queryStart;

        IndexStatistics statistics = engine.statistics();
        double indexMilliseconds = indexElapsed / 1_000_000.0;
        double averageQueryMicroseconds = queryElapsed / 1_000.0 / queryIterations;

        System.out.printf(Locale.ROOT, "documents=%d%n", statistics.documentCount());
        System.out.printf(Locale.ROOT, "uniqueTerms=%d%n", statistics.uniqueTermCount());
        System.out.printf(Locale.ROOT, "postings=%d%n", statistics.postingCount());
        System.out.printf(Locale.ROOT, "indexBuildMs=%.3f%n", indexMilliseconds);
        System.out.printf(Locale.ROOT, "averageQueryMicros=%.3f%n", averageQueryMicroseconds);
        System.out.printf(Locale.ROOT, "resultCount=%d%n", matches.size());
        System.out.printf(Locale.ROOT, "queryIterations=%d%n", queryIterations);
    }

    private static List<Document> corpus(int documentCount) {
        List<Document> documents = new ArrayList<>(documentCount);
        for (int index = 0; index < documentCount; index++) {
            String topic = index % 2 == 0 ? "ranking" : "indexing";
            documents.add(new Document(
                    String.format(Locale.ROOT, "doc-%03d.txt", index),
                    "java search engine experiment " + topic + " document " + index));
        }
        return List.copyOf(documents);
    }

    private static int parsePositiveArgument(String[] args, int index, int defaultValue) {
        if (args.length <= index) {
            return defaultValue;
        }
        int parsed = Integer.parseInt(args[index]);
        if (parsed < 1) {
            throw new IllegalArgumentException("Arguments must be positive integers");
        }
        return parsed;
    }
}
