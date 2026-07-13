# Mini Search Engine

Build a search system from first principles, measure its behavior, and improve it
through evidence. Java is the initial implementation language.

## First Milestone: Exact Retrieval

Given at least 100 UTF-8 text documents and a query, return the documents that
contain normalized query terms.

### Initial Modules

- Document loading and stable document identifiers
- Text normalization and tokenization
- Inverted index construction
- Exact query evaluation
- Unit and end-to-end tests

### Acceptance Criteria

- A documented command builds and tests the project from a clean checkout.
- Tokenization behavior is deterministic and tested.
- The index maps normalized terms to matching document identifiers.
- Queries return correct matches for a fixture corpus of at least 100 documents.
- Empty, malformed, and no-match queries have explicit behavior.
- A baseline reports indexing time, query latency, and index size.

## Planned Evolution

1. Exact Boolean retrieval
2. TF-IDF or BM25 ranking
3. Relevance evaluation
4. Search API
5. Lexical versus semantic retrieval experiment
