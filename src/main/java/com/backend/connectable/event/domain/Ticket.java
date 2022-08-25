package com.backend.connectable.event.domain;

import com.backend.connectable.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

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

    @Enumerated(EnumType.STRING)
    private TicketSalesStatus ticketSalesStatus;

    @Convert(converter = TicketMetadataConverter.class)
    @Column(columnDefinition = "TEXT")
    private TicketMetadata ticketMetadata;

    @Builder
    public Ticket(Long id, User user, Event event, int tokenId, String tokenUri, int price,
                  TicketSalesStatus ticketSalesStatus, TicketMetadata ticketMetadata) {
        this.id = id;
        this.user = user;
        this.event = event;
        this.tokenId = tokenId;
        this.tokenUri = tokenUri;
        this.price = price;
        this.ticketSalesStatus = ticketSalesStatus;
        this.ticketMetadata = ticketMetadata;
    }

    public String getContractAddress() {
        return event.getContractAddress();
    }

    public void transferredTo(User user) {
        this.user = user;
        this.soldOut();
    }

    public void toPending() {
        this.ticketSalesStatus = this.ticketSalesStatus.toPending();
    }

    public void soldOut() {
        this.ticketSalesStatus = this.ticketSalesStatus.soldOut();
    }

    public void onSale() {
        this.ticketSalesStatus = this.ticketSalesStatus.onSale();
    }

    public LocalDateTime getStartTime() {
        return this.event.getStartTime();
    }

    public Long getEventId() {
        return this.event.getId();
    }

    public String getArtistName() {
        return this.event.getArtistName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(id, ticket.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
