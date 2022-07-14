package com.backend.connectable.event.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventTicket {

    private Long id;
    private int price;
    private String artistName;
    private LocalDateTime eventDate;
    private String eventName;
    private boolean onSale;
    private int tokenId;
    private String tokenUri;
    private String metadata;
    private String contractAddress;
}
