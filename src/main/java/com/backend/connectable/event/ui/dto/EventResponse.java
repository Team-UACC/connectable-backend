package com.backend.connectable.event.ui.dto;

import com.backend.connectable.global.util.DateTimeUtil;
import lombok.*;

import java.time.LocalDateTime;

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
    public EventResponse(
            Long id,
            String name,
            String image,
            LocalDateTime date,
            String description,
            LocalDateTime salesFrom,
            LocalDateTime salesTo) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.date = DateTimeUtil.toEpochMilliSeconds(date);
        this.description = description;
        this.salesFrom = DateTimeUtil.toEpochMilliSeconds(salesFrom);
        this.salesTo = DateTimeUtil.toEpochMilliSeconds(salesTo);
    }
}
