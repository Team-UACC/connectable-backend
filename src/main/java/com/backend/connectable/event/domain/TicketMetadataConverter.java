package com.backend.connectable.event.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import java.io.IOException;

public class TicketMetadataConverter implements AttributeConverter<TicketMetadata, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Override
    public String convertToDatabaseColumn(TicketMetadata attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("티켓 메타데이터 -> JSON 실패");
        }
    }

    @Override
    public TicketMetadata convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, TicketMetadata.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("JSON -> 티켓 메타데이터 실패");
        }
    }
}
