package io.github.akhil0072.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class RankingEvaluationTest {

    @Test
    void calculatesPrecisionAtCutoffFromPositiveJudgments() {
        Map<String, Integer> judgments = Map.of("a", 3, "c", 1, "d", 2);

        double precision = RankingMetrics.precisionAtK(
                List.of("a", "b", "c"), judgments, 3);

        assertEquals(2.0 / 3.0, precision, 0.000_001);
    }

    @Test
    void normalizesDiscountedGainAgainstTheIdealRanking() {
        Map<String, Integer> judgments = Map.of("a", 3, "b", 2, "c", 1);

        assertEquals(
                1.0,
                RankingMetrics.ndcgAtK(List.of("a", "b", "c"), judgments, 3),
                0.000_001);
        double reversed = RankingMetrics.ndcgAtK(List.of("c", "b", "a"), judgments, 3);
        assertTrue(reversed > 0.0 && reversed < 1.0);
    }

    @Test
    void comparesExactRetrievalWithBm25OnTheSameJudgments() {
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

        assertEquals(2, summary.queryCount());
        assertEquals(1.0 / 3.0, summary.exact().precisionAtK(), 0.000_001);
        assertEquals(1.0, summary.bm25().precisionAtK(), 0.000_001);
        assertTrue(summary.bm25().ndcgAtK() > summary.exact().ndcgAtK());
    }

    @Test
    void rejectsInvalidEvaluationInputs() {
        assertThrows(
                IllegalArgumentException.class,
                () -> RankingMetrics.precisionAtK(List.of(), Map.of(), 0));
        assertThrows(
                IllegalArgumentException.class,
                () -> new EvaluationQuery(" ", Map.of("a", 1)));
        assertThrows(
                IllegalArgumentException.class,
                () -> new EvaluationQuery("java", Map.of("a", -1)));
    }
}
