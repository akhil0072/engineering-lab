package io.github.akhil0072.search;

public record MetricScore(double precisionAtK, double ndcgAtK) {

    public MetricScore {
        validateUnitInterval(precisionAtK, "precisionAtK");
        validateUnitInterval(ndcgAtK, "ndcgAtK");
    }

    private static void validateUnitInterval(double value, String name) {
        if (!Double.isFinite(value) || value < 0.0 || value > 1.0) {
            throw new IllegalArgumentException(name + " must be between zero and one");
        }
    }
}
