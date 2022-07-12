package com.backend.connectable.event.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TicketMetadata {
    private String name;
    private String description;
    private String image;
    private List<TicketMetadataAttribute> attributes;

    @Builder
    public TicketMetadata(String name, String description, String image, Map<String, String> attributes) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.attributes = attributes.entrySet().stream()
                .map(attribute -> TicketMetadataAttribute.builder()
                        .trait_type(attribute.getKey())
                        .value(attribute.getValue())
                        .build())
                .collect(Collectors.toList());
    }
}
