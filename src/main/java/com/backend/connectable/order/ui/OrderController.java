package com.backend.connectable.order.ui;

import com.backend.connectable.exception.sequence.ValidationSequence;
import com.backend.connectable.order.service.OrderService;
import com.backend.connectable.order.ui.dto.OrderRequest;
import com.backend.connectable.order.ui.dto.OrderResponse;
import com.backend.connectable.security.ConnectableUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
        @AuthenticationPrincipal ConnectableUserDetails userDetails,
        @RequestBody @Validated(ValidationSequence.class) OrderRequest request
    ) {
        OrderResponse orderResponse = orderService.createOrder(userDetails, request);
        return ResponseEntity.ok(orderResponse);
    }
}
