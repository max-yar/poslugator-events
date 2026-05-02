package com.poslugator.events.task;

import java.time.Instant;
import java.util.UUID;

public record ExecutionRejectedEvent(UUID eventId, String taskId, Instant timestamp) implements TaskEvent {}
