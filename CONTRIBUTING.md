# Contributing

## Change Workflow

1. Define one observable outcome.
2. Create a focused branch and keep the change small.
3. Add or update tests when behavior changes.
4. Run the narrowest relevant checks during development and the full suite before review.
5. Explain what changed, how it was verified, and any remaining risk.
6. Merge only when the branch is understandable and reproducible.

## Commit Style

Use an imperative subject that describes the outcome:

```text
Add exact-match document retrieval
Measure tokenizer throughput
Document BM25 evaluation method
```

Avoid commits that mix unrelated cleanup, generated artifacts, and behavioral changes.

## Definition of Done

A change is done when:

- The intended behavior works.
- Relevant tests or measurements pass.
- Documentation reflects important decisions.
- Temporary files, secrets, and local data are excluded.
- Another engineer can reproduce the result from the repository.
