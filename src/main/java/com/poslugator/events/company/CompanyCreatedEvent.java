package com.poslugator.events.company;

import java.time.Instant;
import java.util.UUID;

public record CompanyCreatedEvent(UUID eventId, long companyId, long ownerId, Instant timestamp)
        implements CompanyEvent {}
