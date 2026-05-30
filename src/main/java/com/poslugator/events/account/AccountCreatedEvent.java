package com.poslugator.events.account;

import java.time.Instant;
import java.util.UUID;

public record AccountCreatedEvent(
        UUID eventId,
        long accountId,
        String email,
        String userName,
        Instant timestamp
) implements AccountEvent {}
