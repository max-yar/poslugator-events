package com.poslugator.events.company;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.time.Instant;
import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "eventType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = CompanyCreatedEvent.class, name = "COMPANY_CREATED"),
    @JsonSubTypes.Type(value = CompanyDeletedEvent.class, name = "COMPANY_DELETED"),
    @JsonSubTypes.Type(value = CompanyOwnerChangedEvent.class, name = "COMPANY_OWNER_CHANGED"),
})
public sealed interface CompanyEvent permits CompanyCreatedEvent, CompanyDeletedEvent, CompanyOwnerChangedEvent {
    UUID eventId();

    long companyId();

    Instant timestamp();
}
