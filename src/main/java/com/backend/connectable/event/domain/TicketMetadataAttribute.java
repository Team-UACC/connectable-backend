package com.backend.connectable.event.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketMetadataAttribute {
    private String trait_type;
    private String value;
}
