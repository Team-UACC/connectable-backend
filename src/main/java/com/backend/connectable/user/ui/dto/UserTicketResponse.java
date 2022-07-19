package com.backend.connectable.user.ui.dto;

import com.backend.connectable.event.domain.TicketMetadata;
import com.backend.connectable.event.domain.TicketSalesStatus;
import com.backend.connectable.global.common.util.DateTimeUtil;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserTicketResponse {

    private Long id;
    private int price;
    private Long eventDate;
    private String eventName;
    private TicketSalesStatus ticketSalesStatus;
    private int tokenId;
    private String tokenUri;
    private TicketMetadata metadata;
    private String contractAddress;
    private Long eventId;
    private String artistName;

    @Builder
    public UserTicketResponse(Long id, int price, LocalDateTime eventDate, String eventName, TicketSalesStatus ticketSalesStatus, int tokenId, String tokenUri, TicketMetadata metadata, String contractAddress, Long eventId, String artistName) {
        this.id = id;
        this.price = price;
        this.eventDate = DateTimeUtil.toEpochMilliSeconds(eventDate);
        this.eventName = eventName;
        this.ticketSalesStatus = ticketSalesStatus;
        this.tokenId = tokenId;
        this.tokenUri = tokenUri;
        this.metadata = metadata;
        this.contractAddress = contractAddress;
        this.eventId = eventId;
        this.artistName = artistName;
    }
}
