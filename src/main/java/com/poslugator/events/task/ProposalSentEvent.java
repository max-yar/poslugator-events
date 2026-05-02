package com.poslugator.events.task;

import java.time.Instant;
import java.util.UUID;

public record ProposalSentEvent(
        UUID eventId, String taskId, String actorName, String subjectEmail, Instant timestamp)
        implements TaskEvent {}
