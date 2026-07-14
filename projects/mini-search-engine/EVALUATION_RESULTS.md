# Evaluation Results

Run on 2026-07-13 with the corpus and judgments defined in `EvaluationRunner`:

```text
queries=2 cutoff=3
exact precision@k=0.333333 ndcg@k=0.745253
bm25 precision@k=1.000000 ndcg@k=0.972121
```

On this corpus, BM25 retrieves more judged-relevant documents in the first three results and orders them closer to the ideal graded ranking than exact AND retrieval.

See `EVALUATION.md` for the method and limitations. These figures are a regression baseline for this synthetic experiment, not a production-quality claim.
