package com.ticketrush.inventory_service.messaging;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record Message(String messageId, String type, String bookingId,
                      Instant occurredAt, JsonNode payload)
{
    // record : class which holds data. means it auto create constructor with ARG, Getters, equals(), toString(). / alternative of LOMBOK
    // JsonNode : used to hold flexible json payload rather than predefined payload schema
    // valueToTree : converts incoming map to json structure
    // Instant.now() : returns current DATE&TIME instant
    // of() : simple method with appropriate naming convenion. Eg Message.of(_ _ _ _); becoz it is static.

    public static Message of(String type, String bookingId,Map<String, Object> payload, ObjectMapper mapper)
    {
        return new Message(UUID.randomUUID().toString(), type, bookingId,
                Instant.now(), mapper.valueToTree(payload));
    }
}
