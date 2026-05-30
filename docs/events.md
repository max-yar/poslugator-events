# Task Events

## Overview

All task domain events implement the `TaskEvent` sealed interface (`com.poslugator.events.task`). Every event is published to the Kafka topic `task-events` (see `Topics.TASK_EVENTS`).

## Common fields

Every `TaskEvent` carries these fields regardless of type:

| Field | Type | Description |
|---|---|---|
| `eventId` | `UUID` | Unique identifier for this event instance |
| `taskId` | `String` | Identifier of the task this event belongs to |
| `taskTitle` | `String` | Human-readable title of the task at the time of the event |
| `actorName` | `String` | Display name of the user who triggered the action |
| `subjectId` | `Long` | App user ID of the notification recipient. Stable identity — used by downstream services to key notifications without relying on email (Google-auth users may have no email). |
| `subjectEmail` | `String` | Email of the notification recipient. Present for email delivery but must not be used as the primary identity key. |
| `timestamp` | `Instant` | UTC instant when the event occurred |

Record constructor order: `(UUID eventId, String taskId, String taskTitle, String actorName, Long subjectId, String subjectEmail, Instant timestamp)`.

## Serialization

The JSON payload includes an `eventType` discriminator field whose value matches the constants listed below. Jackson resolves the correct concrete record type from this field.

Example payload:

```json
{
  "eventType": "PROPOSAL_SENT",
  "eventId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "taskId": "task-123",
  "taskTitle": "Fix the roof",
  "actorName": "Ivan Kovalenko",
  "subjectId": 42,
  "subjectEmail": "client@example.com",
  "timestamp": "2026-05-21T10:15:30Z"
}
```

## Event catalog

| `eventType` | Java class | Trigger |
|---|---|---|
| `PROPOSAL_SENT` | `ProposalSentEvent` | A master submits a proposal on a task |
| `PROPOSAL_ACCEPTED` | `ProposalAcceptedEvent` | The client accepts a master's proposal |
| `PROPOSAL_REJECTED` | `ProposalRejectedEvent` | The client rejects a master's proposal |
| `EXECUTION_STARTED` | `ExecutionStartedEvent` | The master marks execution as started |
| `EXECUTION_COMPLETED` | `ExecutionCompletedEvent` | The master marks execution as completed |
| `EXECUTION_CONFIRMED` | `ExecutionConfirmedEvent` | The client confirms the completed execution |
| `EXECUTION_REJECTED` | `ExecutionRejectedEvent` | The client rejects the completed execution |
| `EXECUTION_CANCELLED` | `ExecutionCancelledEvent` | An execution is cancelled mid-way |
| `TASK_CANCELLED` | `TaskCancelledEvent` | The task itself is cancelled |
| `TASK_REOPENED` | `TaskReopenedEvent` | A cancelled task is reopened |
| `REVIEW_CREATED` | `ReviewCreatedEvent` | A review is submitted for a completed task |

## Kafka topic

```
task-events
```

Defined as the constant `Topics.TASK_EVENTS` in `com.poslugator.events.topics.Topics`.

## Producer

`poslugator-back` publishes all 11 event types to this topic via `com.poslugator.kafka.TaskEventProducer`
(`@TransactionalEventListener(phase = AFTER_COMMIT)`). Message key is `event.taskId()`. Both services
use `poslugator-events:0.2.0`.

`subjectId` is set by `TaskEventFactory` to `subject.getId()` when a distinct subject user exists,
or to `actor.getId()` when the actor is also the notification recipient (self-directed events).

## Diagram

See [class-task-events.mmd](diagrams/class-task-events.mmd) for the type hierarchy.
