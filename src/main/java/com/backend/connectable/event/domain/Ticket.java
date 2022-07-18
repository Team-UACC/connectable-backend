package com.backend.connectable.event.domain;

import com.backend.connectable.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Event event;

    private int tokenId;

    private String tokenUri;

    private int price;

    private boolean onSale;

    @Convert(converter = TicketMetadataConverter.class)
    @Column(columnDefinition = "TEXT")
    private TicketMetadata ticketMetadata;

    @Builder
    public Ticket(Long id, User user, Event event, int tokenId, String tokenUri, int price, boolean onSale, TicketMetadata ticketMetadata) {
        this.id = id;
        this.user = user;
        this.event = event;
        this.tokenId = tokenId;
        this.tokenUri = tokenUri;
        this.price = price;
        this.onSale = onSale;
        this.ticketMetadata = ticketMetadata;
    }
}
