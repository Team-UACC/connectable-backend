package com.backend.connectable.event.domain.dto;

import com.backend.connectable.event.domain.SalesOption;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventDetail {

    private Long id;
    private String eventName;
    private String eventImage;
    private String artistName;
    private String artistImage;
    private String description;
    private String contractAddress;
    private String contractName;
    private LocalDateTime salesFrom;
    private LocalDateTime salesTo;
    private String twitterUrl;
    private String instagramUrl;
    private String webpageUrl;
    private Integer totalTicketCount;
    private Integer onSaleTicketCount;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int price;
    private String location;
    private SalesOption salesOption;
}
