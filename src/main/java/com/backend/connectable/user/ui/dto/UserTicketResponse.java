package com.backend.connectable.user.ui.dto;

import com.backend.connectable.event.domain.Ticket;
import com.backend.connectable.event.domain.TicketMetadata;
import com.backend.connectable.event.domain.TicketSalesStatus;
import com.backend.connectable.global.util.DateTimeUtil;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    public UserTicketResponse(
            Long id,
            int price,
            LocalDateTime eventDate,
            String eventName,
            TicketSalesStatus ticketSalesStatus,
            int tokenId,
            String tokenUri,
            TicketMetadata metadata,
            String contractAddress,
            Long eventId,
            String artistName) {
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

    public static List<UserTicketResponse> toList(List<Ticket> tickets) {
        return tickets.stream().map(UserTicketResponse::of).collect(Collectors.toList());
    }

    public static UserTicketResponse of(Ticket ticket) {
        return UserTicketResponse.builder()
                .id(ticket.getId())
                .price(ticket.getPrice())
                .eventDate(ticket.getStartTime())
                .ticketSalesStatus(ticket.getTicketSalesStatus())
                .tokenId(ticket.getTokenId())
                .tokenUri(ticket.getTokenUri())
                .metadata(ticket.getTicketMetadata())
                .contractAddress(ticket.getContractAddress())
                .eventId(ticket.getEventId())
                .artistName(ticket.getArtistName())
                .build();
    }
}
