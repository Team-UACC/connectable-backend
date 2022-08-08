package com.backend.connectable.order.domain;

import com.backend.connectable.exception.ConnectableException;
import com.backend.connectable.exception.ErrorType;
import org.springframework.http.HttpStatus;

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
        throw new ConnectableException(HttpStatus.BAD_REQUEST, ErrorType.ORDER_TO_PAID_UNAVAILABLE);
    }

    public OrderStatus toUnpaid() {
        if (this.isRequested()) {
            return UNPAID;
        }
        throw new ConnectableException(HttpStatus.BAD_REQUEST, ErrorType.ORDER_TO_UNPAID_UNAVAILABLE);
    }

    public OrderStatus toRefund() {
        if (this.isRequested()) {
            return REFUND;
        }
        throw new ConnectableException(HttpStatus.BAD_REQUEST, ErrorType.ORDER_TO_REFUND_UNAVAILABLE);
    }

    public OrderStatus toTransferSuccess() {
        if (this.isPaid()) {
            return TRANSFER_SUCCESS;
        }
        throw new ConnectableException(HttpStatus.BAD_REQUEST, ErrorType.ORDER_TO_TRANSFER_SUCCESS_UNAVAILABLE);
    }

    public OrderStatus toTransferFail() {
        if (this.isPaid()) {
            return TRANSFER_FAIL;
        }
        throw new ConnectableException(HttpStatus.BAD_REQUEST, ErrorType.ORDER_TO_TRANSFER_FAIL_UNAVAILABLE);
    }
}
