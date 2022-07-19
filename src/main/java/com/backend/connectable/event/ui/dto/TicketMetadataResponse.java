package com.backend.connectable.event.ui.dto;

import com.backend.connectable.event.domain.TicketMetadata;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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
}
