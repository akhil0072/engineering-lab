package io.github.akhil0072.search;

import java.util.Objects;

public record SearchResult(Document document, double score) {

    public SearchResult {
        Objects.requireNonNull(document, "document");
        if (!Double.isFinite(score) || score < 0.0) {
            throw new IllegalArgumentException("score must be finite and non-negative");
        }
    }
}
