package com.backend.connectable.auth.ui;

import com.backend.connectable.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/sms/key")
    public ResponseEntity<String> getAuthKey(
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("duration") Long duration) {
        String authKey = authService.getAuthKey(phoneNumber, duration);
        return ResponseEntity.ok(authKey);
    }

    @GetMapping("/sms/certification")
    public ResponseEntity<Boolean> certifyAuthKey(
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("authKey") String authKey) {
        boolean isCertified = authService.certifyKey(phoneNumber, authKey);
        return ResponseEntity.ok(isCertified);
    }
}
