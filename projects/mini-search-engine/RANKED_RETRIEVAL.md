# Ranked Retrieval

The ranked-search path uses BM25 to order documents by estimated relevance while
the existing `search` method retains exact multi-term AND semantics.

## Contract

- `rankedSearch(query, limit)` returns documents containing any normalized query
  term.
- Results are ordered by descending BM25 score.
- Equal scores are ordered by document ID for reproducibility.
- The result limit must be positive.
- Blank, punctuation-only, and no-match queries return an empty list.
- Repeated query terms are treated as one term.

## Scoring

The index records term frequency per document, document frequency per term, and
the normalized token length of every document. Scoring uses:

- `k1 = 1.2` for term-frequency saturation.
- `b = 0.75` for document-length normalization.
- Robertson-Sparck Jones inverse document frequency with a positive BM25 offset.

These conventional defaults establish a baseline. They are not yet tuned for a
specific corpus.

## Evidence

Behavioral tests verify that:

- evidence from multiple query terms increases rank;
- repeated document terms help with diminishing returns;
- ties and limits are deterministic;
- invalid and empty inputs have explicit behavior;
- exact retrieval behavior and index statistics remain unchanged.

## Next Experiment

The next slice will add a relevance-labeled corpus and calculate ranking-quality
metrics such as Precision at K and normalized discounted cumulative gain. That
evidence will let us compare BM25 against the exact-retrieval baseline and test
parameter changes rather than assuming they improve search quality.
