package com.backend.connectable.event.domain;

import com.backend.connectable.exception.ConnectableException;
import com.backend.connectable.exception.ErrorType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;

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
            throw new ConnectableException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.TICKET_METADATA_TO_JSON_FAILURE);
        }
    }

    @Override
    public TicketMetadata convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, TicketMetadata.class);
        } catch (IOException e) {
            throw new ConnectableException(HttpStatus.INTERNAL_SERVER_ERROR, ErrorType.TICKET_JSON_TO_METADATA_FAILURE);
        }
    }
}
