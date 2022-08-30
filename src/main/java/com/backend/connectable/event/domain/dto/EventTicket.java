package com.backend.connectable.event.domain.dto;

import com.backend.connectable.event.domain.TicketMetadata;
import com.backend.connectable.event.domain.TicketSalesStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Convert;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class EventTicket {

    private Long id;
    private int price;
    private String artistName;
    private LocalDateTime eventDate;
    private String eventName;
    private TicketSalesStatus ticketSalesStatus;
    private int tokenId;
    private String tokenUri;
    private boolean isUsed;
    @Convert(converter = TicketMetadata.class)
    private TicketMetadata metadata;
    private String contractAddress;

    @QueryProjection
    public EventTicket(Long id, int price, String artistName, LocalDateTime eventDate, String eventName,
                       TicketSalesStatus ticketSalesStatus, int tokenId, String tokenUri, boolean isUsed,
                       TicketMetadata metadata, String contractAddress) {
        this.id = id;
        this.price = price;
        this.artistName = artistName;
        this.eventDate = eventDate;
        this.eventName = eventName;
        this.ticketSalesStatus = ticketSalesStatus;
        this.tokenId = tokenId;
        this.tokenUri = tokenUri;
        this.isUsed = isUsed;
        this.metadata = metadata;
        this.contractAddress = contractAddress;
    }

    public boolean getIsUsed() {
        return isUsed;
    }

    public boolean isUsed() {
        return isUsed;
    }
}
