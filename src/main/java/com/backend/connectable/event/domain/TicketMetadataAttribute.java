package com.backend.connectable.event.domain;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TicketMetadataAttribute {
    private String trait_type;
    private String value;
}
