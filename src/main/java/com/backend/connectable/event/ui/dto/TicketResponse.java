package com.backend.connectable.event.ui.dto;

import com.backend.connectable.event.domain.TicketMetadata;
import com.backend.connectable.event.domain.TicketSalesStatus;
import com.backend.connectable.global.common.util.DateTimeUtil;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponse {

    private Long id;
    private int price;
    private String artistName;
    private Long eventDate;
    private String eventName;
    private TicketSalesStatus ticketSalesStatus;
    private int tokenId;
    private String tokenUri;
    private boolean isUsed;
    private TicketMetadataResponse metadata;
    private String contractAddress;
    private String ownedBy;

    @Builder
    public TicketResponse(
            Long id,
            int price,
            String artistName,
            LocalDateTime eventDate,
            String eventName,
            TicketSalesStatus ticketSalesStatus,
            int tokenId,
            String tokenUri,
            boolean isUsed,
            TicketMetadata metadata,
            String contractAddress,
            String ownedBy) {
        this.id = id;
        this.price = price;
        this.artistName = artistName;
        this.eventDate = DateTimeUtil.toEpochMilliSeconds(eventDate);
        this.eventName = eventName;
        this.ticketSalesStatus = ticketSalesStatus;
        this.tokenId = tokenId;
        this.tokenUri = tokenUri;
        this.isUsed = isUsed;
        this.metadata = TicketMetadataResponse.of(metadata);
        this.contractAddress = contractAddress;
        this.ownedBy = ownedBy;
    }
}
