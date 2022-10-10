package com.backend.connectable.admin.ui;

import com.backend.connectable.admin.service.AdminService;
import com.backend.connectable.admin.ui.dto.EventIssueRequest;
import com.backend.connectable.admin.ui.dto.TokenIssueRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @PatchMapping("/order-details/{order-detail-id}/paid")
    public ResponseEntity<Void> orderDetailToPaid(
            @PathVariable("order-detail-id") Long orderDetailId) {
        adminService.orderDetailToPaid(orderDetailId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/order-details/{order-detail-id}/unpaid")
    public ResponseEntity<Void> orderDetailToUnpaid(
            @PathVariable("order-detail-id") Long orderDetailId) {
        adminService.orderDetailToUnpaid(orderDetailId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/order-details/{order-detail-id}/refund")
    public ResponseEntity<Void> orderDetailToRefund(
            @PathVariable("order-detail-id") Long orderDetailId) {
        adminService.orderDetailToRefund(orderDetailId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/deploy-event")
    public ResponseEntity<Void> deployEvent(
            @Valid @RequestBody EventIssueRequest eventIssueRequest) {
        adminService.issueEvent(eventIssueRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/mint-tokens")
    public ResponseEntity<Void> mintTokens(
            @Valid @RequestBody TokenIssueRequest tokenIssueRequest) {
        adminService.issueTokens(tokenIssueRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
