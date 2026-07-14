package io.github.akhil0072.search;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class RankingMetrics {
    private RankingMetrics() {}

    public static double precisionAtK(
            List<String> rankedDocumentIds,
            Map<String, Integer> relevanceByDocumentId,
            int cutoff) {
        validateInputs(rankedDocumentIds, relevanceByDocumentId, cutoff);
        int relevant = 0;
        for (int index = 0; index < Math.min(cutoff, rankedDocumentIds.size()); index++) {
            if (relevanceByDocumentId.getOrDefault(rankedDocumentIds.get(index), 0) > 0) {
                relevant++;
            }
        }
        return relevant / (double) cutoff;
    }

    public static double ndcgAtK(
            List<String> rankedDocumentIds,
            Map<String, Integer> relevanceByDocumentId,
            int cutoff) {
        validateInputs(rankedDocumentIds, relevanceByDocumentId, cutoff);
        double actualGain = 0.0;
        for (int index = 0; index < Math.min(cutoff, rankedDocumentIds.size()); index++) {
            int grade = relevanceByDocumentId.getOrDefault(rankedDocumentIds.get(index), 0);
            actualGain += discountedGain(grade, index);
        }

        List<Integer> idealGrades = relevanceByDocumentId.values().stream()
                .sorted(Comparator.reverseOrder())
                .limit(cutoff)
                .toList();
        double idealGain = 0.0;
        for (int index = 0; index < idealGrades.size(); index++) {
            idealGain += discountedGain(idealGrades.get(index), index);
        }
        return idealGain == 0.0 ? 0.0 : actualGain / idealGain;
    }

    private static double discountedGain(int grade, int zeroBasedRank) {
        double gain = Math.pow(2.0, grade) - 1.0;
        double discount = Math.log(zeroBasedRank + 2.0) / Math.log(2.0);
        return gain / discount;
    }

    private static void validateInputs(
            List<String> rankedDocumentIds,
            Map<String, Integer> relevanceByDocumentId,
            int cutoff) {
        Objects.requireNonNull(rankedDocumentIds, "rankedDocumentIds");
        Objects.requireNonNull(relevanceByDocumentId, "relevanceByDocumentId");
        if (cutoff < 1) {
            throw new IllegalArgumentException("cutoff must be positive");
        }
        Set<String> uniqueIds = new HashSet<>(rankedDocumentIds);
        if (uniqueIds.size() != rankedDocumentIds.size()) {
            throw new IllegalArgumentException("ranked document ids must be unique");
        }
    }
}
