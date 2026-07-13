package io.github.akhil0072.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

class SearchEngineTest {

    @Test
    void findsDocumentContainingNormalizedTerm() {
        Document document = new Document("guide.txt", "Java powers this search engine.");
        SearchEngine engine = SearchEngine.from(List.of(document));

        assertEquals(List.of(document), engine.search("JAVA"));
    }

    @Test
    void returnsDocumentsContainingAllTermsInIdOrder() {
        Document second = new Document("b.txt", "Java makes search practical.");
        Document first = new Document("a.txt", "Search systems can be written in Java.");
        Document partial = new Document("c.txt", "Java without the other term.");
        SearchEngine engine = SearchEngine.from(List.of(second, partial, first));

        assertEquals(List.of(first, second), engine.search("search java"));
    }

    @Test
    void returnsNoMatchesForQueryWithoutSearchableTerms() {
        SearchEngine engine = SearchEngine.from(
                List.of(new Document("guide.txt", "Searchable content")));

        assertEquals(List.of(), engine.search("  !!!  "));
    }

    @Test
    void returnsNoMatchesWhenAnyRequiredTermIsAbsent() {
        SearchEngine engine = SearchEngine.from(
                List.of(new Document("guide.txt", "Java search guide")));

        assertEquals(List.of(), engine.search("java missing"));
    }

    @Test
    void normalizesUnicodeCaseAndPunctuation() {
        Document document = new Document("unicode.txt", "Caf\u00e9 search notes");
        SearchEngine engine = SearchEngine.from(List.of(document));

        assertEquals(List.of(document), engine.search("CAFE\u0301!!!"));
    }

    @Test
    void rejectsDuplicateDocumentIds() {
        List<Document> documents = List.of(
                new Document("same.txt", "first"),
                new Document("same.txt", "second"));

        assertThrows(IllegalArgumentException.class, () -> SearchEngine.from(documents));
    }

    @Test
    void reportsAggregateIndexStatistics() {
        SearchEngine engine = SearchEngine.from(List.of(
                new Document("a.txt", "java search java"),
                new Document("b.txt", "java storage")));

        assertEquals(new IndexStatistics(2, 3, 4), engine.statistics());
    }
}
