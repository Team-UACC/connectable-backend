package com.backend.connectable.event.ui.dto;

import com.backend.connectable.event.domain.EventSalesOption;
import com.backend.connectable.global.common.util.DateTimeUtil;
import com.backend.connectable.global.common.util.OpenseaCollectionNamingUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
public class EventDetailResponse {

    private Long id;
    private String name;
    private String image;
    private String artistName;
    private String artistImage;
    private String description;
    private String contractAddress;
    private String openseaUrl;
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
    private EventSalesOption eventSalesOption;

    @Builder
    public EventDetailResponse(Long id, String name, String image, String artistName, String artistImage, String description,
                               String contractAddress, String contractName, LocalDateTime salesFrom, LocalDateTime salesTo, String twitterUrl,
                               String instagramUrl, String webpageUrl, int totalTicketCount, int onSaleTicketCount,
                               LocalDateTime startTime, LocalDateTime endTime, int price, String location, EventSalesOption eventSalesOption) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.artistName = artistName;
        this.artistImage = artistImage;
        this.description = description;
        this.contractAddress = contractAddress;
        this.openseaUrl = OpenseaCollectionNamingUtil.toOpenseaCollectionUrl(contractName);
        this.salesFrom = DateTimeUtil.toEpochMilliSeconds(salesFrom);
        this.salesTo = DateTimeUtil.toEpochMilliSeconds(salesTo);
        this.twitterUrl = twitterUrl;
        this.instagramUrl = instagramUrl;
        this.webpageUrl = webpageUrl;
        this.totalTicketCount = totalTicketCount;
        this.onSaleTicketCount = onSaleTicketCount;
        this.startTime = DateTimeUtil.toEpochMilliSeconds(startTime);
        this.endTime = DateTimeUtil.toEpochMilliSeconds(endTime);
        this.price = price;
        this.location = location;
        this.eventSalesOption = eventSalesOption;
    }
}
