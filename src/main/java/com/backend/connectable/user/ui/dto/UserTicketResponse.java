package com.backend.connectable.user.ui.dto;

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
        this.eventDate = DateTimeUtil.toEpochMilliSeconds(eventDate);
        this.eventName = eventName;
        this.onSale = onSale;
        this.tokenId = tokenId;
        this.tokenUri = tokenUri;
        this.metadata = metadata;
        this.contractAddress = contractAddress;
        this.eventId = eventId;
    }
}
