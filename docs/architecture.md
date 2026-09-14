# Architecture

## Purpose

`poslugator-events` is a shared Java library that defines the event contract for the ProfiHub platform. It contains no runtime logic — only type definitions and the Kafka topic constant. Consumers depend on it as a compile-time artifact to produce and consume strongly-typed events without duplicating the schema.

## Distribution

The library is published via [JitPack](https://jitpack.io). The version is derived from git tags using the [axion-release](https://github.com/allegro/axion-release-plugin) plugin.

```
groupId:    com.poslugator
artifactId: poslugator-events
version:    <git tag>
```

Consumers add the JitPack repository and declare a dependency:

```kotlin
repositories {
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.github.ProfiHub:poslugator-events:<version>")
}
```

## Technology

| Concern | Choice |
|---|---|
| Language | Java 21 |
| Serialization | Jackson Databind 3.x (`tools.jackson.core`) |
| Build | Gradle 8 (Kotlin DSL) + axion-release |
| Distribution | JitPack |
| Code format | palantir-java-format 2.90.0 via Spotless |

## Package structure

```
com.poslugator.events
├── account/       # AccountEvent sealed interface and all concrete event records
├── task/          # TaskEvent sealed interface and all concrete event records
├── chat/          # ChatMessageEvent — plain record, no sealed interface, no Jackson annotations
└── topics/        # Topics — Kafka topic name constants (TASK_EVENTS, ACCOUNT_EVENTS, CHAT_EVENTS)
```

## Design decisions

**Sealed interface hierarchies** — both `TaskEvent` and `AccountEvent` are `sealed interface`s permitting exactly the known concrete record types. This gives exhaustive `switch` expressions in consumers and prevents third-party subtypes.

**Interface-level method on `AccountEvent`** — `userName()` is declared on `AccountEvent` directly (not just on individual records), so consumers can call it without pattern-matching. `TaskEvent` declares its own common accessors directly on the sealed interface too (`eventId()`, `taskId()`, `taskTitle()`, `actor()`, `subjectId()`, `subjectEmail()`, `timestamp()`) — every subtype shares the same shape, unlike `AccountEvent`'s per-subtype extra fields (e.g. `verificationToken`).

**Jackson polymorphism** — `@JsonTypeInfo(use=NAME, property="eventType")` + `@JsonSubTypes` on both sealed interfaces embed an `eventType` discriminator field in the JSON payload with SCREAMING_SNAKE_CASE values. No Kafka type headers are used; the discriminator is always body-embedded.

**Records for concrete events** — all event types are `record` implementations of their respective sealed interface. Records are immutable, require no boilerplate, and serialize cleanly with Jackson.

**No Spring / no runtime dependency** — the library is intentionally framework-agnostic so any JVM service can consume it regardless of their application framework.

**`ChatMessageEvent` breaks the pattern on purpose** — it is a plain record with no `@JsonTypeInfo`/`@JsonSubTypes` and no sealed interface, unlike `TaskEvent`/`AccountEvent`. Producer (`poslugator-chat`) and consumer (`poslugator-notification`) run different Jackson majors, and an annotation-free record is what serializes identically on both. See [chat-events.md](chat-events.md).

## Diagrams

- [Task event type hierarchy](diagrams/class-task-events.mmd)
- [Account event type hierarchy](diagrams/class-account-events.mmd)
- [Task lifecycle event sequence](diagrams/sequence-task-lifecycle.mmd)
