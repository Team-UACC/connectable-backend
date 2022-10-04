package com.backend.connectable.fixture;

import com.backend.connectable.event.domain.Event;
import com.backend.connectable.event.domain.Ticket;
import com.backend.connectable.event.domain.TicketMetadata;
import com.backend.connectable.event.domain.TicketSalesStatus;

import java.util.HashMap;

public class TicketFixture {

    private static final int TICKET_PRICE = 100000;
    private static final String TOKEN_URI_FORMAT = "https://connectable-events.s3.ap-northeast-2.amazonaws.com/json/%d.json";
    private static final String IMAGE_URI_FORMAT = "https://connectable-events.s3.ap-northeast-2.amazonaws.com/ticket_test%d.png";

    private TicketFixture() {
    }

    public static Ticket createTicket(Event event, int tokenId) {
        return Ticket.builder()
            .event(event)
            .tokenUri(String.format(TOKEN_URI_FORMAT, tokenId))
            .tokenId(tokenId)
            .price(TICKET_PRICE)
            .ticketSalesStatus(TicketSalesStatus.PENDING)
            .ticketMetadata(generateTicketMetadata(event, tokenId))
            .build();
    }

    private static TicketMetadata generateTicketMetadata(Event event, int tokenId) {
        return TicketMetadata.builder()
            .name(event.getEventName())
            .description(event.getDescription())
            .image(String.format(IMAGE_URI_FORMAT, tokenId))
            .attributes(generateAttributes(event, tokenId))
            .build();
    }

    private static HashMap<String, String> generateAttributes(Event event, int tokenId) {
        return new HashMap<>() {
            {
                put("Seat", String.format("Seat#%d", tokenId));
                put("Artist", event.getArtistName());
            }
        };
    }
}
