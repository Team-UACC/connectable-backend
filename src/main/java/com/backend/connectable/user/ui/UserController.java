package com.backend.connectable.user.ui;

import com.backend.connectable.security.ConnectableUserDetails;
import com.backend.connectable.user.service.UserService;
import com.backend.connectable.user.ui.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/users/login")
    public ResponseEntity<UserLoginResponse> loginUser(@RequestBody UserLoginRequest userLoginRequest) {
        UserLoginResponse userLoginResponse = userService.login(userLoginRequest);
        if (userLoginResponse.checkStatusFailed()) {
            return ResponseEntity.badRequest().body(userLoginResponse);
        }
        return ResponseEntity.ok(userLoginResponse);
    }

    @GetMapping("/users")
    public ResponseEntity<UserResponse> getUser(@AuthenticationPrincipal ConnectableUserDetails userDetails) {
        UserResponse userResponse = userService.getUserByUserDetails(userDetails);
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping("/users")
    public ResponseEntity<UserModifyResponse> deleteUser(@AuthenticationPrincipal ConnectableUserDetails userDetails) {
        UserModifyResponse userModifyResponse = userService.deleteUserByUserDetails(userDetails);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(userModifyResponse);
    }

    @PutMapping("/users")
    public ResponseEntity<UserModifyResponse> modifyUser(@AuthenticationPrincipal ConnectableUserDetails userDetails,
                                                         @RequestBody UserModifyRequest userModifyRequest) {
        UserModifyResponse userModifyResponse = userService.modifyUserByUserDetails(userDetails, userModifyRequest);
        return ResponseEntity.status(HttpStatus.OK).body(userModifyResponse);
    }
}
