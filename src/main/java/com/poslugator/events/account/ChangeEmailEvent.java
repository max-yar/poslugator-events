package com.poslugator.events.account;

import java.time.Instant;
import java.util.UUID;

public record ChangeEmailEvent(
        UUID eventId,
        long accountId,
        String userName,
        Instant timestamp
) implements AccountEvent {}
