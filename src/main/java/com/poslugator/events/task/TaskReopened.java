package com.poslugator.events.task;

import java.time.Instant;
import java.util.UUID;

public record TaskReopened(UUID eventId, String taskId, Instant timestamp) implements TaskEvent {}
