package com.backend.connectable.user.ui;

import com.backend.connectable.user.service.UserService;
import com.backend.connectable.user.ui.dto.UserDeleteResponse;
import com.backend.connectable.user.ui.dto.UserLoginRequest;
import com.backend.connectable.user.ui.dto.UserLoginResponse;
import com.backend.connectable.user.ui.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users/login")
    public ResponseEntity<UserLoginResponse> loginUser(@RequestBody UserLoginRequest userLoginRequest) {
        UserLoginResponse userLoginResponse = userService.login(userLoginRequest);
        if (userLoginResponse.isFailed()) {
            return ResponseEntity.badRequest().body(userLoginResponse);
        }
        return ResponseEntity.ok(userLoginResponse);
    }

    @GetMapping("/users")
    public ResponseEntity<UserResponse> getUser() {
        String klaytnAddress = "0x1234";
        UserResponse userResponse = userService.getUserByWalletAddress(klaytnAddress);
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/users")
    public ResponseEntity<UserDeleteResponse> deleteUser() {
        String klaytnAddress = "0x1234";
        UserDeleteResponse userDeleteResponse = userService.deleteUserByKlaytnAddress(klaytnAddress);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(userDeleteResponse);
    }
}
