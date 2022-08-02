package com.backend.connectable.order.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.*;

class OrderDetailTest {

    @DisplayName("OrderDetail의 orderStatus가 Request인 경우 Paid로 변경이 가능하다.")
    @Test
    void toPaid() {
        // given
        OrderDetail orderDetail = OrderDetail.builder()
            .orderStatus(OrderStatus.REQUESTED)
            .build();

        // when
        assertThatCode(() -> orderDetail.paid())
            .doesNotThrowAnyException();
    }

    @DisplayName("OrderDetail의 orderStatus가 Request가 아닌 경우 Paid로 변경이 불가능하다.")
    @Test
    void toPaidFail() {
        // given
        OrderDetail orderDetail = OrderDetail.builder()
            .orderStatus(OrderStatus.TRANSFER_FAIL)
            .build();

        // when & then
        assertThatThrownBy(() -> orderDetail.paid())
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderDetail의 orderStatus가 Request인 경우 unpaid로 변경이 가능하다.")
    @Test
    void toUnpaid() {
        // given
        OrderDetail orderDetail = OrderDetail.builder()
            .orderStatus(OrderStatus.REQUESTED)
            .build();

        // when
        assertThatCode(() -> orderDetail.unpaid())
            .doesNotThrowAnyException();
    }

    @DisplayName("OrderDetail의 orderStatus가 Request가 아닌 경우 unpaid로 변경이 불가능하다.")
    @Test
    void toUnpaidFail() {
        // given
        OrderDetail orderDetail = OrderDetail.builder()
            .orderStatus(OrderStatus.TRANSFER_FAIL)
            .build();

        // when & then
        assertThatThrownBy(() -> orderDetail.unpaid())
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderDetail의 orderStatus가 Request인 경우 refund로 변경이 가능하다.")
    @Test
    void toRefund() {
        // given
        OrderDetail orderDetail = OrderDetail.builder()
            .orderStatus(OrderStatus.REQUESTED)
            .build();

        // when
        assertThatCode(() -> orderDetail.refund())
            .doesNotThrowAnyException();
    }

    @DisplayName("OrderDetail의 orderStatus가 Request가 아닌 경우 refund로 변경이 불가능하다.")
    @Test
    void toRefundFail() {
        // given
        OrderDetail orderDetail = OrderDetail.builder()
            .orderStatus(OrderStatus.TRANSFER_FAIL)
            .build();

        // when & then
        assertThatThrownBy(() -> orderDetail.refund())
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderDetail의 orderStatus가 paid 경우 transferSuccess로 변경이 가능하다.")
    @Test
    void toTransferSuccess() {
        // given
        OrderDetail orderDetail = OrderDetail.builder()
            .orderStatus(OrderStatus.PAID)
            .build();

        // when
        assertThatCode(() -> orderDetail.transferSuccess())
            .doesNotThrowAnyException();
    }

    @DisplayName("OrderDetail의 orderStatus가 paid 아닌 경우 transferSuccess로 변경이 불가능하다.")
    @Test
    void toTransferSuccessFail() {
        // given
        OrderDetail orderDetail = OrderDetail.builder()
            .orderStatus(OrderStatus.REQUESTED)
            .build();

        // when & then
        assertThatThrownBy(() -> orderDetail.transferSuccess())
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("OrderDetail의 orderStatus가 paid 경우 transferFail로 변경이 가능하다.")
    @Test
    void toTransferFail() {
        // given
        OrderDetail orderDetail = OrderDetail.builder()
            .orderStatus(OrderStatus.PAID)
            .build();

        // when
        assertThatCode(() -> orderDetail.transferFail())
            .doesNotThrowAnyException();
    }

    @DisplayName("OrderDetail의 orderStatus가 paid 아닌 경우 transferFail로 변경이 불가능하다.")
    @Test
    void toTransferFailFail() {
        // given
        OrderDetail orderDetail = OrderDetail.builder()
            .orderStatus(OrderStatus.REQUESTED)
            .build();

        // when & then
        assertThatThrownBy(() -> orderDetail.transferFail())
            .isInstanceOf(IllegalArgumentException.class);
    }
}