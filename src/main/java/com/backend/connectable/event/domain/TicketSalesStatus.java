package com.backend.connectable.event.domain;

import com.backend.connectable.exception.ConnectableException;
import com.backend.connectable.exception.ErrorType;
import org.springframework.http.HttpStatus;

public enum TicketSalesStatus {
    ON_SALE, PENDING, SOLD_OUT, EXPIRED;

    public TicketSalesStatus toPending() {
        if (this.equals(ON_SALE)) {
            return PENDING;
        }
        throw new ConnectableException(HttpStatus.BAD_REQUEST, ErrorType.TICKET_TO_PENDING_UNAVAILABLE);
    }

    public TicketSalesStatus soldOut() {
        if (this.equals(PENDING)) {
            return SOLD_OUT;
        }
        throw new ConnectableException(HttpStatus.BAD_REQUEST, ErrorType.TICKET_TO_SOLD_OUT_UNAVAILABLE);
    }

    public TicketSalesStatus onSale() {
        if (this.equals(PENDING)) {
            return ON_SALE;
        }
        throw new ConnectableException(HttpStatus.BAD_REQUEST, ErrorType.TICKET_TO_ON_SALE_UNAVAILABLE);
    }
}
