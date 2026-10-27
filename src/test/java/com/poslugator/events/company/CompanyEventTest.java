package com.poslugator.events.company;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

class CompanyEventTest {

    private static final JsonMapper MAPPER = JsonMapper.builder().build();
    private static final UUID EVENT_ID = UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6");
    private static final long COMPANY_ID = 17L;
    private static final long OWNER_ID = 3L;
    private static final long PREVIOUS_OWNER_ID = 5L;
    private static final Instant TIMESTAMP = Instant.parse("2026-10-27T10:15:30Z");

    @Test
    void shouldWriteCompanyCreatedTypeAndFieldsWhenCompanyCreatedEventSerialized() {
        var event = new CompanyCreatedEvent(EVENT_ID, COMPANY_ID, OWNER_ID, TIMESTAMP);

        var json = MAPPER.readTree(MAPPER.writeValueAsString(event));

        var expected = MAPPER.readTree("""
                {"eventType": "COMPANY_CREATED", "eventId": "%s", "companyId": %d, "ownerId": %d,
                 "timestamp": "%s"}
                """.formatted(EVENT_ID, COMPANY_ID, OWNER_ID, TIMESTAMP));
        assertEquals(expected, json);
    }

    @Test
    void shouldReadCompanyCreatedEventWhenDeserializedAsCompanyEvent() {
        var event = new CompanyCreatedEvent(EVENT_ID, COMPANY_ID, OWNER_ID, TIMESTAMP);

        var restored = MAPPER.readValue(MAPPER.writeValueAsString(event), CompanyEvent.class);

        assertEquals(event, restored);
    }

    @Test
    void shouldWriteCompanyOwnerChangedTypeAndFieldsWhenCompanyOwnerChangedEventSerialized() {
        var event = new CompanyOwnerChangedEvent(EVENT_ID, COMPANY_ID, OWNER_ID, PREVIOUS_OWNER_ID, TIMESTAMP);

        var json = MAPPER.readTree(MAPPER.writeValueAsString(event));

        var expected = MAPPER.readTree("""
                {"eventType": "COMPANY_OWNER_CHANGED", "eventId": "%s", "companyId": %d, "ownerId": %d,
                 "previousOwnerId": %d, "timestamp": "%s"}
                """.formatted(EVENT_ID, COMPANY_ID, OWNER_ID, PREVIOUS_OWNER_ID, TIMESTAMP));
        assertEquals(expected, json);
    }

    @Test
    void shouldReadCompanyOwnerChangedEventWhenDeserializedAsCompanyEvent() {
        var event = new CompanyOwnerChangedEvent(EVENT_ID, COMPANY_ID, OWNER_ID, PREVIOUS_OWNER_ID, TIMESTAMP);

        var restored = MAPPER.readValue(MAPPER.writeValueAsString(event), CompanyEvent.class);

        assertEquals(event, restored);
    }

    @Test
    void shouldWriteCompanyDeletedTypeAndFieldsWhenCompanyDeletedEventSerialized() {
        var event = new CompanyDeletedEvent(EVENT_ID, COMPANY_ID, TIMESTAMP);

        var json = MAPPER.readTree(MAPPER.writeValueAsString(event));

        var expected = MAPPER.readTree("""
                {"eventType": "COMPANY_DELETED", "eventId": "%s", "companyId": %d, "timestamp": "%s"}
                """.formatted(EVENT_ID, COMPANY_ID, TIMESTAMP));
        assertEquals(expected, json);
    }

    @Test
    void shouldReadCompanyDeletedEventWhenDeserializedAsCompanyEvent() {
        var event = new CompanyDeletedEvent(EVENT_ID, COMPANY_ID, TIMESTAMP);

        var restored = MAPPER.readValue(MAPPER.writeValueAsString(event), CompanyEvent.class);

        assertEquals(event, restored);
    }
}
