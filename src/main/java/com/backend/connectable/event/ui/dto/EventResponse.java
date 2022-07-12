package com.backend.connectable.event.ui.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EventResponse {

    private Long id;
    private String name;
    private String image;
    private Long date;
    private String description;
    private Long salesFrom;
    private Long salesTo;
}
