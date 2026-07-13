# Roadmap

The roadmap is organized around evidence, not course completion. Each milestone
must produce working software, tests or measurements, and a concise explanation.

## Now: Repository Foundation

- Establish repository structure and engineering standards.
- Track work through small, outcome-oriented GitHub issues.
- Keep `main` in a reviewable, working state.

## Next: Mini Search Engine

### Milestone 1 - Exact Retrieval

Given at least 100 text documents and a query, return matching documents with
automated tests for tokenization, indexing, and retrieval.

### Milestone 2 - Ranked Retrieval

Add TF-IDF or BM25 ranking, define relevance judgments, and report baseline
quality and latency.

### Milestone 3 - Service Boundary

Expose search through an API with validation, structured errors, logging, and
integration tests.

### Milestone 4 - Hybrid Search Experiment

Compare lexical and semantic retrieval using the same dataset and evaluation
method. Publish the trade-offs and failed assumptions.

## Later: Systems Depth

- Build one concurrency or distributed-systems project.
- Contribute a meaningful fix to an established open-source repository.
- Graduate the strongest lab project into a standalone flagship repository.
