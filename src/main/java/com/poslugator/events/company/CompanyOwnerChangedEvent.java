package com.poslugator.events.company;

import java.time.Instant;
import java.util.UUID;

public record CompanyOwnerChangedEvent(
        UUID eventId, long companyId, long ownerId, long previousOwnerId, Instant timestamp) implements CompanyEvent {}
