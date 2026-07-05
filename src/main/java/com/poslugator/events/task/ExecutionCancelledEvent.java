package com.poslugator.events.task;

import java.time.Instant;
import java.util.UUID;

public record ExecutionCancelledEvent(
        UUID eventId,
        String taskId,
        String taskTitle,
        UserPreview actor,
        Long subjectId,
        String subjectEmail,
        Instant timestamp)
        implements TaskEvent {}
