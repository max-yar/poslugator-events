# Company Events

## Overview

All company lifecycle events implement the `CompanyEvent` sealed interface (`com.poslugator.events.company`), added in `0.7.0`. Every event is published to the Kafka topic `company-events` (see `Topics.COMPANY_EVENTS`).

## Common fields

Every `CompanyEvent` carries these fields regardless of type:

| Field | Type | Description |
|---|---|---|
| `eventId` | `UUID` | Unique identifier for this event instance |
| `companyId` | `long` | Identifier of the company this event belongs to; also the Kafka message key |
| `timestamp` | `Instant` | UTC instant when the event occurred |

Every event except `COMPANY_DELETED` also carries `ownerId` — the full "who answers for the company" state at the time of the event. A consumer applies the latest state with an idempotent upsert and never has to combine a delta with what it saw before.

## Serialization

`CompanyEvent` uses the same Jackson polymorphic pattern as `TaskEvent` and `AccountEvent`. The `@JsonTypeInfo` annotation embeds an `eventType` discriminator field in the JSON payload. Consumers can deserialize directly to `CompanyEvent`; Jackson resolves the concrete record from the discriminator value. No Kafka type headers are required — the type is carried in the JSON body.

Example payload (`COMPANY_CREATED`):

```json
{
  "eventType": "COMPANY_CREATED",
  "eventId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "companyId": 17,
  "ownerId": 3,
  "timestamp": "2026-10-27T10:15:30Z"
}
```

Example payload (`COMPANY_OWNER_CHANGED`):

```json
{
  "eventType": "COMPANY_OWNER_CHANGED",
  "eventId": "7cb1a2b3-1234-5678-abcd-ef0123456789",
  "companyId": 17,
  "ownerId": 5,
  "previousOwnerId": 3,
  "timestamp": "2026-10-27T11:00:00Z"
}
```

## Event catalog

| `eventType` | Java class | Extra fields | Trigger |
|---|---|---|---|
| `COMPANY_CREATED` | `CompanyCreatedEvent` | `ownerId` | A company is created. Also re-published for every existing company by the producer's snapshot |
| `COMPANY_OWNER_CHANGED` | `CompanyOwnerChangedEvent` | `ownerId`, `previousOwnerId` | Ownership of the company moves to another user |
| `COMPANY_DELETED` | `CompanyDeletedEvent` | — | The company is deleted |

"Extra fields" lists fields present on that record beyond the common set.

### Record constructors

```
CompanyCreatedEvent(UUID eventId, long companyId, long ownerId, Instant timestamp)
CompanyOwnerChangedEvent(UUID eventId, long companyId, long ownerId, long previousOwnerId, Instant timestamp)
CompanyDeletedEvent(UUID eventId, long companyId, Instant timestamp)
```

## Kafka topic

```
company-events
```

Defined as the constant `Topics.COMPANY_EVENTS` in `com.poslugator.events.topics.Topics`. The message key is `companyId`, so Kafka keeps the events of one company in order. The topic keeps 7 days and its dead-letter topic `company-events-dlt` 30 days, like the other topics (`profihub-infra` `kafka-init.sh`); there is no compaction, so rebuilding chat's copy from scratch needs a new snapshot from back.

## Producer and consumer

`poslugator-back` is the producer and `poslugator-chat` the sole consumer; chat keeps the members of each company inbox from these events.

| `eventType` | Emitted by (`poslugator-back`) | Consumed by (`poslugator-chat`) |
|---|---|---|
| `COMPANY_CREATED` | Company creation, and the snapshot of existing companies | Upserts the owner into the company inbox |
| `COMPANY_OWNER_CHANGED` | Ownership transfer | Replaces the company inbox owner |
| `COMPANY_DELETED` | Company deletion | Deletes the company inbox |

## Diagram

See [class-company-events.mmd](diagrams/class-company-events.mmd) for the type hierarchy.
