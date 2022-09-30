package com.backend.connectable.order.ui;

import com.backend.connectable.exception.sequence.ValidationSequence;
import com.backend.connectable.order.service.OrderService;
import com.backend.connectable.order.ui.dto.OrderDetailResponse;
import com.backend.connectable.order.ui.dto.OrderRequest;
import com.backend.connectable.order.ui.dto.OrderResponse;
import com.backend.connectable.security.custom.ConnectableUserDetails;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @AuthenticationPrincipal ConnectableUserDetails userDetails,
            @RequestBody @Validated(ValidationSequence.class) OrderRequest request) {
        OrderResponse orderResponse = orderService.createOrder(userDetails, request);
        log.info(
                "##USER::{}@@PHONE::{}@@TICKETS::{}",
                request.getUserName(),
                request.getPhoneNumber(),
                StringUtils.join(request.getTicketIds(), ','));
        return ResponseEntity.ok(orderResponse);
    }

    @GetMapping("/list")
    public ResponseEntity<List<OrderDetailResponse>> getOrderDetailList(
            @AuthenticationPrincipal ConnectableUserDetails userDetails) {
        List<OrderDetailResponse> orderDetailResponses =
                orderService.getOrderDetailList(userDetails);
        return ResponseEntity.ok(orderDetailResponses);
    }
}
