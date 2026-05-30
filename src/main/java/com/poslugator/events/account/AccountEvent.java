package com.poslugator.events.account;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.time.Instant;
import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "eventType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = AccountCreatedEvent.class, name = "ACCOUNT_CREATED"),
    @JsonSubTypes.Type(value = VerifyEmailEvent.class, name = "VERIFY_EMAIL"),
    @JsonSubTypes.Type(value = ChangeEmailEvent.class, name = "CHANGE_EMAIL"),
    @JsonSubTypes.Type(value = ForgotPasswordEvent.class, name = "FORGOT_PASSWORD"),
    @JsonSubTypes.Type(value = AddPasswordEvent.class, name = "ADD_PASSWORD"),
    @JsonSubTypes.Type(value = ChangePasswordEvent.class, name = "CHANGE_PASSWORD"),
    @JsonSubTypes.Type(value = DeleteAccountEvent.class, name = "DELETE_ACCOUNT"),
})
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
