package io.github.akhil0072.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class RankedSearchTest {

    @Test
    void ranksDocumentsUsingAllAvailableQueryEvidence() {
        Document bothTerms = new Document("both.txt", "java search");
        Document oneTerm = new Document("one.txt", "java only");
        SearchEngine engine = SearchEngine.from(List.of(oneTerm, bothTerms));

        List<SearchResult> results = engine.rankedSearch("java search", 10);

        assertEquals(List.of(bothTerms, oneTerm), documents(results));
        assertTrue(results.get(0).score() > results.get(1).score());
    }

    @Test
    void rewardsTermFrequencyWithoutAllowingItToGrowLinearly() {
        Document repeated = new Document("repeated.txt", "java java java");
        Document single = new Document("single.txt", "java filler filler");
        SearchEngine engine = SearchEngine.from(List.of(single, repeated));

        List<SearchResult> results = engine.rankedSearch("java", 10);

        assertEquals(List.of(repeated, single), documents(results));
        assertTrue(results.get(0).score() < results.get(1).score() * 3.0);
    }

    @Test
    void breaksEqualScoreTiesByDocumentIdAndAppliesLimit() {
        Document second = new Document("b.txt", "java");
        Document first = new Document("a.txt", "java");
        SearchEngine engine = SearchEngine.from(List.of(second, first));

        List<SearchResult> results = engine.rankedSearch("java", 1);

        assertEquals(List.of(first), documents(results));
    }

    @Test
    void returnsNoRankedResultsForQueryWithoutSearchableTerms() {
        SearchEngine engine = SearchEngine.from(List.of(new Document("a.txt", "java")));

        assertEquals(List.of(), engine.rankedSearch("!!!", 10));
        assertEquals(List.of(), engine.rankedSearch("missing", 10));
    }

    @Test
    void rejectsNonPositiveResultLimit() {
        SearchEngine engine = SearchEngine.from(List.of(new Document("a.txt", "java")));

        assertThrows(IllegalArgumentException.class, () -> engine.rankedSearch("java", 0));
    }

    private static List<Document> documents(List<SearchResult> results) {
        return results.stream().map(SearchResult::document).toList();
    }
}
