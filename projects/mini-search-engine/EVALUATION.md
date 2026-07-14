# Ranked Retrieval Evaluation

This experiment compares exact AND retrieval with BM25 ranking on the same labeled queries.
It is a reproducible development check, not a claim about production search quality.

## Method

- Corpus: 6 synthetic documents covering Java/search and distributed-storage topics.
- Queries: `java search` and `distributed storage`.
- Relevance grades: 3 = highly relevant, 2 = relevant, 1 = partially relevant, and 0 or unjudged = not relevant.
- Cutoff: K = 3.
- Baseline: exact retrieval requires every query term to occur.
- Candidate: BM25 ranks documents using term frequency, inverse document frequency, and document-length normalization.

Precision@K treats every positive grade as relevant and divides relevant results by K.
NDCG@K uses graded gain `2^grade - 1`, discounts lower ranks logarithmically, and normalizes against the ideal ranking.

## Run

From this directory:

```powershell
mvn clean verify
java -cp target/classes io.github.akhil0072.search.EvaluationRunner
```

Expected output is recorded after running the evaluator on the committed implementation.

## Limitations

- The corpus and judgments are small, synthetic, and authored by the developer.
- Two queries are not statistically representative of real search traffic.
- Unjudged documents are treated as not relevant.
- The comparison isolates ranking behavior; it does not measure indexing speed, query latency, memory, typo tolerance, stemming, or semantic relevance.
- A meaningful external result needs a larger independently judged dataset and repeated performance measurements.

## Next Experiment

Expand the corpus and query set, separate judgments from implementation work, and benchmark indexing and ranked-query latency with JMH.
