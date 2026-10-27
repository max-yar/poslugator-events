package com.poslugator.events.company;

import java.time.Instant;
import java.util.UUID;

public record CompanyDeletedEvent(UUID eventId, long companyId, Instant timestamp) implements CompanyEvent {}
