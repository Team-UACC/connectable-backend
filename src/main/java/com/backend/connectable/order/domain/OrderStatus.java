package com.backend.connectable.order.domain;

public enum OrderStatus {

    REQUESTED, PAID, UNPAID, REFUND, TRANSFER_SUCCESS, TRANSFER_FAIL;

    public boolean isRequested() {
        return this.equals(REQUESTED);
    }

    public boolean isPaid() {
        return this.equals(PAID);
    }

    public OrderStatus toPaid() {
        if (this.isRequested()) {
            return PAID;
        }
        throw new IllegalArgumentException("PAID 상태로 변경이 불가합니다.");
    }

    public OrderStatus toUnpaid() {
        if (this.isRequested()) {
            return UNPAID;
        }
        throw new IllegalArgumentException("UNPAID 상태로 변경이 불가합니다.");
    }

    public OrderStatus toRefund() {
        if (this.isRequested()) {
            return REFUND;
        }
        throw new IllegalArgumentException("REFUND 상태로 변경이 불가합니다.");
    }

    public OrderStatus toTransferSuccess() {
        if (this.isPaid()) {
            return TRANSFER_SUCCESS;
        }
        throw new IllegalArgumentException("TRANSFER SUCCESS 상태로 변경이 불가합니다.");
    }

    public OrderStatus toTransferFail() {
        if (this.isPaid()) {
            return TRANSFER_FAIL;
        }
        throw new IllegalArgumentException("TRANSFER FAIL 상태로 변경이 불가합니다.");
    }
}
