# Exact Retrieval Baseline

## Environment

- Date: 2026-07-13
- Operating system: Windows 11
- Runtime: OpenJDK 17.0.18
- Corpus: 100 deterministic in-memory UTF-8 documents
- Query: `java search`
- Warm-up: 1,000 queries
- Measured queries: 10,000

## Result

```text
documents=100
uniqueTerms=107
postings=700
indexBuildMs=18.667
averageQueryMicros=14.987
resultCount=100
queryIterations=10000
```

The logical index size is 107 unique terms and 700 term-to-document postings.
All 100 documents match the two-term AND query.

## Reproduce

```text
mvn -q -DskipTests package
java -cp target/classes io.github.akhil0072.search.BaselineRunner 100 10000
```

## Limitations

This is a directional baseline, not a statistically rigorous benchmark. It uses
one JVM process, synthetic documents, a small corpus, and wall-clock timing.
Hardware load, JIT compilation, and garbage collection can change the timing.
Future ranking work should use JMH and a relevance-labeled corpus.
