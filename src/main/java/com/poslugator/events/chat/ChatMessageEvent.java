package com.poslugator.events.chat;

import java.time.Instant;
import java.util.UUID;

public record ChatMessageEvent(
        UUID eventId,
        long recipientId,
        long senderId,
        long conversationId,
        long messageId,
        String bodyPreview,
        Instant timestamp) {}
