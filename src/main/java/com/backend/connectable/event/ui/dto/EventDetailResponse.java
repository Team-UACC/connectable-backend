package com.backend.connectable.event.ui.dto;

import com.backend.connectable.event.domain.SalesOption;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;

@NoArgsConstructor
@Getter
@Setter
public class EventDetailResponse {

    private Long id;
    private String name;
    private String image;
    private String artistName;
    private Long date;
    private String description;
    private Long salesFrom;
    private Long salesTo;
    private String twitterUrl;
    private String instagramUrl;
    private String webpageUrl;
    private Integer totalTicketCount;
    private Integer onSaleTicketCount;
    private Long startTime;
    private Long endTime;
    private int price;
    private String location;
    private SalesOption salesOption;

    @Builder
    public EventDetailResponse(Long id, String name, String image, String artistName, LocalDateTime date, String description, LocalDateTime salesFrom, LocalDateTime salesTo, String twitterUrl, String instagramUrl, String webpageUrl, int totalTicketCount, int onSaleTicketCount, LocalDateTime startTime, LocalDateTime endTime, int price, String location, SalesOption salesOption) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.artistName = artistName;
        this.date = date.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
        this.description = description;
        this.salesFrom = salesFrom.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
        this.salesTo = salesTo.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
        this.twitterUrl = twitterUrl;
        this.instagramUrl = instagramUrl;
        this.webpageUrl = webpageUrl;
        this.totalTicketCount = totalTicketCount;
        this.onSaleTicketCount = onSaleTicketCount;
        this.startTime = startTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
        this.endTime = endTime.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
        this.price = price;
        this.location = location;
        this.salesOption = salesOption;
    }
}
