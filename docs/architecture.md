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
└── topics/        # Topics — Kafka topic name constants
```

## Design decisions

**Sealed interface hierarchy** — `TaskEvent` and `AccountEvent` are `sealed interface` types permitting exactly the known concrete record types. This gives exhaustive `switch` expressions in consumers and prevents third-party subtypes.

**Jackson polymorphism** — `@JsonTypeInfo` + `@JsonSubTypes` on both sealed interfaces embed an `eventType` discriminator field in the JSON payload. Consumers deserialize to the interface type directly and receive the correct concrete record. The discriminator values use SCREAMING_SNAKE_CASE and live in the JSON body; no Kafka type headers are required.

**Records for concrete events** — all event types are `record` implementations of their respective interface. Records are immutable, require no boilerplate, and serialize cleanly with Jackson.

**No Spring / no runtime dependency** — the library is intentionally framework-agnostic so any JVM service can consume it regardless of their application framework.

## Diagrams

- [Task event type hierarchy](diagrams/class-task-events.mmd)
- [Account event type hierarchy](diagrams/class-account-events.mmd)
- [Task lifecycle event sequence](diagrams/sequence-task-lifecycle.mmd)
