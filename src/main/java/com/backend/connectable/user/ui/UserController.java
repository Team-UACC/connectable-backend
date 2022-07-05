package com.backend.connectable.user.ui;

import com.backend.connectable.user.service.UserService;
import com.backend.connectable.user.ui.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<UserResponse> getUser() {
        String klaytnAddress = "0x1234";
        UserResponse userResponse = userService.getUserByWalletAddress(klaytnAddress);
        return ResponseEntity.ok(userResponse);
    }
}
