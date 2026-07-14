package io.github.akhil0072.search;

import java.util.List;
import java.util.Objects;

public final class RankingEvaluator {
    private RankingEvaluator() {}

    public static EvaluationSummary compare(
            SearchEngine engine,
            List<EvaluationQuery> queries,
            int cutoff) {
        Objects.requireNonNull(engine, "engine");
        Objects.requireNonNull(queries, "queries");
        if (queries.isEmpty()) {
            throw new IllegalArgumentException("queries must not be empty");
        }
        if (cutoff < 1) {
            throw new IllegalArgumentException("cutoff must be positive");
        }

        double exactPrecision = 0.0;
        double exactNdcg = 0.0;
        double bm25Precision = 0.0;
        double bm25Ndcg = 0.0;
        for (EvaluationQuery query : queries) {
            Objects.requireNonNull(query, "query");
            List<String> exactIds = engine.search(query.query()).stream()
                    .map(Document::id)
                    .limit(cutoff)
                    .toList();
            List<String> bm25Ids = engine.rankedSearch(query.query(), cutoff).stream()
                    .map(result -> result.document().id())
                    .toList();

            exactPrecision += RankingMetrics.precisionAtK(
                    exactIds, query.relevanceByDocumentId(), cutoff);
            exactNdcg += RankingMetrics.ndcgAtK(
                    exactIds, query.relevanceByDocumentId(), cutoff);
            bm25Precision += RankingMetrics.precisionAtK(
                    bm25Ids, query.relevanceByDocumentId(), cutoff);
            bm25Ndcg += RankingMetrics.ndcgAtK(
                    bm25Ids, query.relevanceByDocumentId(), cutoff);
        }

        double queryCount = queries.size();
        return new EvaluationSummary(
                queries.size(),
                cutoff,
                new MetricScore(exactPrecision / queryCount, exactNdcg / queryCount),
                new MetricScore(bm25Precision / queryCount, bm25Ndcg / queryCount));
    }
}
