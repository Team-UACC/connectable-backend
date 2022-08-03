package com.backend.connectable.event.domain;

public enum TicketSalesStatus {
    ON_SALE, PENDING, SOLD_OUT, EXPIRED;

    public TicketSalesStatus toPending() {
        if (this.equals(ON_SALE)) {
            return PENDING;
        }
        throw new IllegalArgumentException("ON SALE 상태만 PENDING으로 변할 수 있습니다.");
    }
}
