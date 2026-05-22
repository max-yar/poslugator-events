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
├── task/          # TaskEvent sealed interface and all concrete event records
└── topics/        # Topics — Kafka topic name constants
```

## Design decisions

**Sealed interface hierarchy** — `TaskEvent` is a `sealed interface` permitting exactly the known concrete record types. This gives exhaustive `switch` expressions in consumers and prevents third-party subtypes.

**Jackson polymorphism** — `@JsonTypeInfo` + `@JsonSubTypes` on `TaskEvent` embed an `eventType` discriminator field in the JSON payload. Consumers deserialize to `TaskEvent` and receive the correct concrete type.

**Records for concrete events** — all 11 event types are `record` implementations of `TaskEvent`. Records are immutable, require no boilerplate, and serialize cleanly with Jackson.

**No Spring / no runtime dependency** — the library is intentionally framework-agnostic so any JVM service can consume it regardless of their application framework.

## Diagrams

- [Event type hierarchy](diagrams/class-task-events.mmd)
- [Task lifecycle event sequence](diagrams/sequence-task-lifecycle.mmd)
