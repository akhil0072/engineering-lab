# Mini Search Engine

Build a search system from first principles, measure its behavior, and improve it
through evidence. Java is the initial implementation language.

## Requirements

- JDK 17
- Maven 3.9 or newer

## Verify

From this directory:

```text
mvn test
```

That command compiles the project and runs every unit and end-to-end test.

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

## Exact Retrieval Semantics

- Documents have unique, stable string IDs.
- Directory-loaded IDs are sorted paths relative to the corpus root using `/`.
- Text is normalized with Unicode NFKC and lowercased using the root locale.
- Tokens contain Unicode letters or decimal digits; punctuation separates tokens.
- Multi-term queries require every distinct term to be present.
- Results are sorted by document ID.
- Blank, punctuation-only, and no-match queries return an empty list.
- A null query is rejected; duplicate document IDs are rejected.

## Run The Baseline

```text
mvn -q -DskipTests package
java -cp target/classes io.github.akhil0072.search.BaselineRunner 100 10000
```

See [`BASELINE.md`](BASELINE.md) for the recorded result and limitations.

## Planned Evolution

1. Exact Boolean retrieval
2. TF-IDF or BM25 ranking
3. Relevance evaluation
4. Search API
5. Lexical versus semantic retrieval experiment
