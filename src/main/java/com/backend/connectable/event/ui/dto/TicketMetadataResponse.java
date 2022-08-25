package com.backend.connectable.event.ui.dto;

import com.backend.connectable.event.domain.TicketMetadata;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TicketMetadataResponse {

    private String name;
    private String description;
    private String image;
    private List<TicketMetadataAttributeResponse> attributes;

    public static TicketMetadataResponse of(TicketMetadata metadata) {
        return new TicketMetadataResponse(
                metadata.getName(),
                metadata.getDescription(),
                metadata.getImage(),
                TicketMetadataAttributeResponse.toList(metadata.getAttributes())
        );
    }

    public TicketMetadata toTicketMetadata() {
        Map<String, String> ticketMetadataAttribute = attributes.stream()
            .collect(Collectors.toMap(TicketMetadataAttributeResponse::getTrait_type, TicketMetadataAttributeResponse::getValue));
        return TicketMetadata.builder()
            .name(name)
            .description(description)
            .image(image)
            .attributes(ticketMetadataAttribute)
            .build();
    }
}
