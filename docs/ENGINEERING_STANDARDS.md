# Engineering Standards

## Correctness

- Define behavior at module boundaries and test public outcomes.
- Validate inputs and make failure modes explicit.
- Prefer deterministic tests; isolate external services behind interfaces.

## Design

- Keep modules focused and expose the smallest useful interface.
- Use established language and repository conventions before adding abstractions.
- Record decisions whose trade-offs will matter later.

## Evidence

- Benchmark before making performance claims.
- Keep datasets, commands, configuration, and environment assumptions reproducible.
- Report negative and inconclusive results alongside successful ones.

## Reliability and Security

- Never commit credentials, tokens, personal data, or proprietary client material.
- Use least-privilege access for services and repositories.
- Log enough context to diagnose failures without leaking sensitive information.

## Communication

- Write concise READMEs that explain purpose, setup, verification, and limitations.
- Prefer measured outcomes over vague claims.
- Make uncertainty and remaining risk visible.
