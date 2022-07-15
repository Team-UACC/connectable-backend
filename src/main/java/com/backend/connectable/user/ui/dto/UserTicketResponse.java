package com.backend.connectable.user.ui.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserTicketResponse {

    private Long id;
    private int price;
    private Long eventDate;
    private String eventName;
    private boolean onSale;
    private int tokenId;
    private String tokenUri;
    private String metadata;
    private String contractAddress;
    private Long eventId;

    @Builder
    public UserTicketResponse(Long id, int price, LocalDateTime eventDate, String eventName, boolean onSale, int tokenId, String tokenUri, String metadata, String contractAddress, Long eventId) {
        this.id = id;
        this.price = price;
        this.eventDate = eventDate.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond() * 1000L;
        this.eventName = eventName;
        this.onSale = onSale;
        this.tokenId = tokenId;
        this.tokenUri = tokenUri;
        this.metadata = metadata;
        this.contractAddress = contractAddress;
        this.eventId = eventId;
    }
}
