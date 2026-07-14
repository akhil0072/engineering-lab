package io.github.akhil0072.search;

import java.util.Map;
import java.util.Objects;

public record EvaluationQuery(String query, Map<String, Integer> relevanceByDocumentId) {

    public EvaluationQuery {
        Objects.requireNonNull(query, "query");
        Objects.requireNonNull(relevanceByDocumentId, "relevanceByDocumentId");
        if (query.isBlank()) {
            throw new IllegalArgumentException("query must contain searchable text");
        }
        relevanceByDocumentId.forEach((documentId, grade) -> {
            if (documentId == null || documentId.isBlank()) {
                throw new IllegalArgumentException("document ids must not be blank");
            }
            if (grade == null || grade < 0) {
                throw new IllegalArgumentException("relevance grades must be non-negative");
            }
        });
        relevanceByDocumentId = Map.copyOf(relevanceByDocumentId);
    }
}
