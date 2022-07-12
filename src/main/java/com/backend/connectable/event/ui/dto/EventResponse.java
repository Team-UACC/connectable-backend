package com.backend.connectable.event.ui.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventResponse {

    private Long id;
    private String name;
    private String image;
    private Long date;
    private String description;
    private Long salesFrom;
    private Long salesTo;

    @Builder
    public EventResponse(Long id, String name, String image, LocalDateTime date, String description, LocalDateTime salesFrom, LocalDateTime salesTo) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.date = date.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
        this.description = description;
        this.salesFrom = salesFrom.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
        this.salesTo = salesTo.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }
}
