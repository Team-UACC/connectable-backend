package com.backend.connectable.admin.ui;

import com.backend.connectable.admin.service.AdminService;
import com.backend.connectable.admin.ui.dto.EventDeploymentRequest;
import com.backend.connectable.admin.ui.dto.TokenMintingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @PatchMapping("/order-details/{order-detail-id}/paid")
    public ResponseEntity<Void> orderDetailToPaid(@PathVariable("order-detail-id") Long orderDetailId) {
        adminService.orderDetailToPaid(orderDetailId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/order-details/{order-detail-id}/unpaid")
    public ResponseEntity<Void> orderDetailToUnpaid(@PathVariable("order-detail-id") Long orderDetailId) {
        adminService.orderDetailToUnpaid(orderDetailId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/order-details/{order-detail-id}/refund")
    public ResponseEntity<Void> orderDetailToRefund(@PathVariable("order-detail-id") Long orderDetailId) {
        adminService.orderDetailToRefund(orderDetailId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/deploy-event")
    public ResponseEntity<Void> deployEvent(@RequestBody EventDeploymentRequest eventDeploymentRequest) throws InterruptedException {
        adminService.deployEvent(eventDeploymentRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/mint-tokens")
    public ResponseEntity<Void> mintTokens(@RequestBody TokenMintingRequest tokenMintingRequest) {
        adminService.mintTokens(tokenMintingRequest);
        return ResponseEntity.ok().build();
    }
}
