package com.poslugator.events.account;

import java.time.Instant;
import java.util.UUID;

public record VerifyEmailEvent(
        UUID eventId,
        long accountId,
        String userName,
        String verificationToken,
        Instant timestamp
) implements AccountEvent {}
