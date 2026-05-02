package com.poslugator.events.task;

import java.time.Instant;
import java.util.UUID;

public record ExecutionCompletedEvent(
        UUID eventId, String taskId, String taskTitle, String actorName, String subjectEmail, Instant timestamp)
        implements TaskEvent {}
