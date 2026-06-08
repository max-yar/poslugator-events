# poslugator-events

Shared event contract library for the ProfiHub platform.

## Contents

- [Architecture](architecture.md) — purpose, distribution, technology stack, design decisions
- [Task Events](events.md) — event catalog, common fields, serialization format, Kafka topic
- [Account Events](account-events.md) — event catalog, common fields, serialization format, Kafka topic
- [Chat Events](chat-events.md) — `ChatMessageEvent`, fields, serialization, Kafka topic

## Diagrams

- [class-task-events.mmd](diagrams/class-task-events.mmd) — `TaskEvent` sealed interface hierarchy
- [class-account-events.mmd](diagrams/class-account-events.mmd) — `AccountEvent` sealed interface hierarchy
- [sequence-task-lifecycle.mmd](diagrams/sequence-task-lifecycle.mmd) — task lifecycle event flow
