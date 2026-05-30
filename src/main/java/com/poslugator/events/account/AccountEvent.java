package com.poslugator.events.account;

import java.time.Instant;
import java.util.UUID;

public sealed interface AccountEvent
        permits AccountCreatedEvent,
                VerifyEmailEvent,
                ChangeEmailEvent,
                ForgotPasswordEvent,
                AddPasswordEvent,
                ChangePasswordEvent,
                DeleteAccountEvent {
    UUID eventId();

    long accountId();

    String userName();

    Instant timestamp();
}
