package com.backend.connectable.user.ui;

import com.backend.connectable.exception.sequence.ValidationSequence;
import com.backend.connectable.security.ConnectableUserDetails;
import com.backend.connectable.user.service.UserService;
import com.backend.connectable.user.ui.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> loginUser(@RequestBody UserLoginRequest userLoginRequest) {
        UserLoginResponse userLoginResponse = userService.login(userLoginRequest);
        if (userLoginResponse.checkStatusFailed()) {
            return ResponseEntity.badRequest().body(userLoginResponse);
        }
        return ResponseEntity.ok(userLoginResponse);
    }

    @GetMapping
    public ResponseEntity<UserResponse> getUser(@AuthenticationPrincipal ConnectableUserDetails userDetails) {
        UserResponse userResponse = userService.getUserByUserDetails(userDetails);
        return ResponseEntity.ok(userResponse);
    }

    @DeleteMapping
    public ResponseEntity<UserModifyResponse> deleteUser(@AuthenticationPrincipal ConnectableUserDetails userDetails) {
        UserModifyResponse userModifyResponse = userService.deleteUserByUserDetails(userDetails);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(userModifyResponse);
    }

    @PutMapping
    public ResponseEntity<UserModifyResponse> modifyUser(@AuthenticationPrincipal ConnectableUserDetails userDetails,
                                                         @RequestBody @Validated(ValidationSequence.class) UserModifyRequest userModifyRequest) {
        UserModifyResponse userModifyResponse = userService.modifyUserByUserDetails(userDetails, userModifyRequest);
        return ResponseEntity.status(HttpStatus.OK).body(userModifyResponse);
    }

    @GetMapping("/tickets")
    public ResponseEntity<UserTicketListResponse> getUserTickets(@AuthenticationPrincipal ConnectableUserDetails userDetails) {
        UserTicketListResponse userTicketListResponse = userService.getUserTicketsByUserDetails(userDetails);
        return ResponseEntity.ok(userTicketListResponse);
    }
}
