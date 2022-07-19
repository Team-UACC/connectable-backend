package com.backend.connectable.event.ui.dto;

import com.backend.connectable.event.domain.TicketMetadataAttribute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TicketMetadataAttributeResponse {

    private String trait_type;
    private String value;

    public static TicketMetadataAttributeResponse of(TicketMetadataAttribute attribute) {
        return new TicketMetadataAttributeResponse(
                attribute.getTrait_type(),
                attribute.getValue()
        );
    }

    public static List<TicketMetadataAttributeResponse> toList(List<TicketMetadataAttribute> attributes) {
        return attributes.stream()
                .map(TicketMetadataAttributeResponse::of)
                .collect(Collectors.toList());
    }
}
