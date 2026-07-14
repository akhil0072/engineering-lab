package io.github.akhil0072.search;

import java.util.Objects;

public record EvaluationSummary(int queryCount, int cutoff, MetricScore exact, MetricScore bm25) {

    public EvaluationSummary {
        if (queryCount < 1) {
            throw new IllegalArgumentException("queryCount must be positive");
        }
        if (cutoff < 1) {
            throw new IllegalArgumentException("cutoff must be positive");
        }
        Objects.requireNonNull(exact, "exact");
        Objects.requireNonNull(bm25, "bm25");
    }
}
