package com.poslugator.events.task;

import java.time.Instant;
import java.util.UUID;

public record ProposalAcceptedEvent(UUID eventId, String taskId, Instant timestamp) implements TaskEvent {}
