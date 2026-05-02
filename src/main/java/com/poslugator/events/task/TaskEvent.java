package com.poslugator.events.task;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.time.Instant;
import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "evenType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = ExecutionCancelled.class, name = "EXECUTION_CANCELLED"),
    @JsonSubTypes.Type(value = ExecutionCompleted.class, name = "EXECUTION_COMPLETED"),
    @JsonSubTypes.Type(value = ExecutionConfirmed.class, name = "EXECUTION_CONFIRMED"),
    @JsonSubTypes.Type(value = ExecutionRejected.class, name = "EXECUTION_REJECTED"),
    @JsonSubTypes.Type(value = ExecutionStarted.class, name = "EXECUTION_STARTED"),
    @JsonSubTypes.Type(value = ProposalAccepted.class, name = "PROPOSAL_ACCEPTED"),
    @JsonSubTypes.Type(value = ProposalRejected.class, name = "PROPOSAL_REJECTED"),
    @JsonSubTypes.Type(value = ProposalSent.class, name = "PROPOSAL_SENT"),
    @JsonSubTypes.Type(value = ReviewCreated.class, name = "REVIEW_CREATED"),
    @JsonSubTypes.Type(value = TaskCancelled.class, name = "TASK_CANCELLED"),
    @JsonSubTypes.Type(value = TaskReopened.class, name = "TASK_REOPENED"),
})
public sealed interface TaskEvent
        permits ExecutionCancelled,
                ExecutionCompleted,
                ExecutionConfirmed,
                ExecutionRejected,
                ExecutionStarted,
                ProposalAccepted,
                ProposalRejected,
                ProposalSent,
                ReviewCreated,
                TaskCancelled,
                TaskReopened {
    UUID eventId();

    String taskId();

    Instant timestamp();
}
