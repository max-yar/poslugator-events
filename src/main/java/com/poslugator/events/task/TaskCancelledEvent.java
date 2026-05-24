package com.poslugator.events.task;

import java.time.Instant;
import java.util.UUID;

public record TaskCancelledEvent(
        UUID eventId,
        String taskId,
        String taskTitle,
        String actorName,
        Long subjectId,
        String subjectEmail,
        Instant timestamp)
        implements TaskEvent {}
