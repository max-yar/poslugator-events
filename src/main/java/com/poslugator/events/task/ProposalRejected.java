package com.poslugator.events.task;

import java.time.Instant;
import java.util.UUID;

public record ProposalRejected(UUID eventId, String taskId, Instant timestamp) implements TaskEvent {}
