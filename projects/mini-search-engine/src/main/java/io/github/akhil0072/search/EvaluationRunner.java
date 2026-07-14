package io.github.akhil0072.search;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class EvaluationRunner {
    private EvaluationRunner() {}

    public static void main(String[] args) {
        SearchEngine engine = SearchEngine.from(List.of(
                new Document("a-java-search.txt", "java search ranking"),
                new Document("b-java-index.txt", "java inverted index"),
                new Document("c-search-quality.txt", "search ranking quality"),
                new Document("d-storage.txt", "distributed storage replication"),
                new Document("e-distributed.txt", "distributed systems consensus"),
                new Document("f-storage-engine.txt", "storage engine design")));
        List<EvaluationQuery> queries = List.of(
                new EvaluationQuery("java search", Map.of(
                        "a-java-search.txt", 3,
                        "b-java-index.txt", 1,
                        "c-search-quality.txt", 2)),
                new EvaluationQuery("distributed storage", Map.of(
                        "d-storage.txt", 3,
                        "e-distributed.txt", 1,
                        "f-storage-engine.txt", 2)));

        EvaluationSummary summary = RankingEvaluator.compare(engine, queries, 3);
        print(summary);
    }

    private static void print(EvaluationSummary summary) {
        System.out.printf(Locale.ROOT, "queries=%d cutoff=%d%n", summary.queryCount(), summary.cutoff());
        printMetrics("exact", summary.exact());
        printMetrics("bm25", summary.bm25());
    }

    private static void printMetrics(String name, MetricScore metrics) {
        System.out.printf(
                Locale.ROOT,
                "%s precision@k=%.6f ndcg@k=%.6f%n",
                name,
                metrics.precisionAtK(),
                metrics.ndcgAtK());
    }
}
