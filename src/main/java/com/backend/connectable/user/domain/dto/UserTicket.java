package com.backend.connectable.user.domain.dto;

import com.backend.connectable.event.domain.TicketMetadata;
import com.backend.connectable.event.domain.TicketMetadataConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Convert;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserTicket {

    private Long id;
    private int price;
    private LocalDateTime eventDate;
    private String eventName;
    private boolean onSale;
    private int tokenId;
    private String tokenUri;
    @Convert(converter = TicketMetadataConverter.class)
    private TicketMetadata metadata;
    private String contractAddress;
    private Long eventId;
    private String artistName;
}
