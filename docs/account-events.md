# Account Events

## Overview

All account domain events implement the `AccountEvent` sealed interface (`com.poslugator.events.account`). Every event is published to the Kafka topic `account-events` (see `Topics.ACCOUNT_EVENTS`).

## Common fields

Every `AccountEvent` carries these fields regardless of type:

| Field | Type | Description |
|---|---|---|
| `eventId` | `UUID` | Unique identifier for this event instance |
| `accountId` | `long` | Identifier of the account this event belongs to |
| `userName` | `String` | Display name of the account holder at the time of the event |
| `timestamp` | `Instant` | UTC instant when the event occurred |

## Serialization

`AccountEvent` uses the same Jackson polymorphic pattern as `TaskEvent`. The `@JsonTypeInfo` annotation embeds an `eventType` discriminator field in the JSON payload. Consumers can deserialize directly to `AccountEvent`; Jackson resolves the concrete record from the discriminator value. No Kafka type headers are required — the type is carried in the JSON body.

Example payload (`ACCOUNT_CREATED`):

```json
{
  "eventType": "ACCOUNT_CREATED",
  "eventId": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
  "accountId": 42,
  "email": "user@example.com",
  "userName": "Ivan Kovalenko",
  "timestamp": "2026-05-30T10:15:30Z"
}
```

Example payload (`VERIFY_EMAIL`):

```json
{
  "eventType": "VERIFY_EMAIL",
  "eventId": "7cb1a2b3-1234-5678-abcd-ef0123456789",
  "accountId": 42,
  "userName": "Ivan Kovalenko",
  "verificationToken": "abc123xyz",
  "timestamp": "2026-05-30T10:16:00Z"
}
```

## Event catalog

| `eventType` | Java class | Extra fields | Trigger |
|---|---|---|---|
| `ACCOUNT_CREATED` | `AccountCreatedEvent` | `email` | A new account is registered |
| `VERIFY_EMAIL` | `VerifyEmailEvent` | `verificationToken` | Email verification is requested |
| `FORGOT_PASSWORD` | `ForgotPasswordEvent` | `verificationToken` | A password-reset flow is initiated |
| `CHANGE_PASSWORD` | `ChangePasswordEvent` | — | The account password is changed |
| `ADD_PASSWORD` | `AddPasswordEvent` | — | A password is added (e.g. after OAuth-only signup) |
| `CHANGE_EMAIL` | `ChangeEmailEvent` | — | The account email is changed |
| `DELETE_ACCOUNT` | `DeleteAccountEvent` | — | The account is deleted |

"Extra fields" lists fields present on that record beyond the common set.

### Record constructors

```
AccountCreatedEvent(UUID eventId, long accountId, String email, String userName, Instant timestamp)
VerifyEmailEvent(UUID eventId, long accountId, String userName, String verificationToken, Instant timestamp)
ForgotPasswordEvent(UUID eventId, long accountId, String userName, String verificationToken, Instant timestamp)
ChangePasswordEvent(UUID eventId, long accountId, String userName, Instant timestamp)
AddPasswordEvent(UUID eventId, long accountId, String userName, Instant timestamp)
ChangeEmailEvent(UUID eventId, long accountId, String userName, Instant timestamp)
DeleteAccountEvent(UUID eventId, long accountId, String userName, Instant timestamp)
```

## Kafka topic

```
account-events
```

Defined as the constant `Topics.ACCOUNT_EVENTS` in `com.poslugator.events.topics.Topics`.

## Diagram

See [class-account-events.mmd](diagrams/class-account-events.mmd) for the type hierarchy.
