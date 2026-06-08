# Chat Events

## Overview

Chat events carry a single event type, `ChatMessageEvent` (`com.poslugator.events.chat`), published to the
Kafka topic `chat-events` (see `Topics.CHAT_EVENTS`). Unlike `TaskEvent` / `AccountEvent`, this is **not** a
sealed interface family and carries **no `eventType` discriminator** — it is a plain `record` with no Jackson
annotations.

The annotation-free, single-type shape is deliberate: producer (`poslugator-chat`) and consumer
(`poslugator-notification`) run on different Spring Boot major versions, and therefore different Jackson
majors (`com.fasterxml` vs `tools.jackson`). A plain record with no library-specific annotations serializes
and deserializes identically on both sides.

## Fields

`ChatMessageEvent` carries:

| Field | Type | Description |
|---|---|---|
| `eventId` | `UUID` | Unique identifier for this event instance |
| `recipientId` | `long` | App user ID of the offline recipient (Kafka message key) |
| `senderId` | `long` | App user ID of the message sender |
| `conversationId` | `long` | ID of the conversation the message belongs to |
| `messageId` | `long` | Server-assigned ID of the persisted message |
| `bodyPreview` | `String` | Truncated message text (max 200 chars) for the email preview |
| `timestamp` | `Instant` | UTC instant when the message was created |

Record constructor order: `(UUID eventId, long recipientId, long senderId, long conversationId, long messageId, String bodyPreview, Instant timestamp)`.

## Serialization

No `eventType` discriminator. The JSON is the flat record shape.

Example payload:

```json
{
  "eventId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "recipientId": 1002,
  "senderId": 1001,
  "conversationId": 10,
  "messageId": 100,
  "bodyPreview": "hi, are you free tomorrow?",
  "timestamp": "2026-06-01T10:15:30Z"
}
```

## Kafka topic

```
chat-events
```

Defined as the constant `Topics.CHAT_EVENTS` in `com.poslugator.events.topics.Topics`. The dead-letter topic
is `chat-events-dlt` (30-day retention); `chat-events` itself uses 7-day retention.

## Producer

`poslugator-chat` publishes a `ChatMessageEvent` via `com.poslugator.kafka.ChatEventProducer` **only when the
recipient has no active WebSocket session** (offline). Online recipients are delivered the message over
WebSocket and produce no event. Message key is `event.recipientId()`.

## Consumer

`poslugator-notification` consumes the topic via `ChatEventConsumer`, resolves the recipient's email, renders
the `chat-message` email template (poslugator-render), and sends it — gated by a per-recipient Redis cooldown
that suppresses bursts. `bodyPreview` is the only message content carried; full message bodies never leave
`poslugator-chat`.
