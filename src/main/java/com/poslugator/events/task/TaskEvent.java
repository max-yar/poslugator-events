package com.poslugator.events.task;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.time.Instant;
import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "eventType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ExecutionCancelledEvent.class, name = "EXECUTION_CANCELLED"),
    @JsonSubTypes.Type(value = ExecutionCompletedEvent.class, name = "EXECUTION_COMPLETED"),
    @JsonSubTypes.Type(value = ExecutionConfirmedEvent.class, name = "EXECUTION_CONFIRMED"),
    @JsonSubTypes.Type(value = ExecutionRejectedEvent.class, name = "EXECUTION_REJECTED"),
    @JsonSubTypes.Type(value = ExecutionStartedEvent.class, name = "EXECUTION_STARTED"),
    @JsonSubTypes.Type(value = ProposalAcceptedEvent.class, name = "PROPOSAL_ACCEPTED"),
    @JsonSubTypes.Type(value = ProposalRejectedEvent.class, name = "PROPOSAL_REJECTED"),
    @JsonSubTypes.Type(value = ProposalSentEvent.class, name = "PROPOSAL_SENT"),
    @JsonSubTypes.Type(value = ReviewCreatedEvent.class, name = "REVIEW_CREATED"),
    @JsonSubTypes.Type(value = TaskCancelledEvent.class, name = "TASK_CANCELLED"),
    @JsonSubTypes.Type(value = TaskReopenedEvent.class, name = "TASK_REOPENED"),
})
public sealed interface TaskEvent
        permits ExecutionCancelledEvent,
        ExecutionCompletedEvent,
        ExecutionConfirmedEvent,
        ExecutionRejectedEvent,
        ExecutionStartedEvent,
        ProposalAcceptedEvent,
        ProposalRejectedEvent,
        ProposalSentEvent,
        ReviewCreatedEvent,
        TaskCancelledEvent,
        TaskReopenedEvent {
    UUID eventId();

    String taskId();

    Instant timestamp();
}
