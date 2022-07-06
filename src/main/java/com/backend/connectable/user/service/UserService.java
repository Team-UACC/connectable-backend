package com.backend.connectable.user.service;

import com.backend.connectable.klip.service.KlipService;
import com.backend.connectable.klip.service.dto.KlipAuthLoginResponse;
import com.backend.connectable.security.JwtProvider;
import com.backend.connectable.user.domain.User;
import com.backend.connectable.user.domain.UserRepository;
import com.backend.connectable.user.ui.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final KlipService klipService;
    private final JwtProvider jwtProvider;

    public UserLoginResponse login(UserLoginRequest userLoginRequest) {
        String requestKey = userLoginRequest.getRequestKey();
        KlipAuthLoginResponse klipAuthLoginResponse = klipService.authLogin(requestKey);
        if (klipAuthLoginResponse.isPrepared()) {
            return UserLoginResponse.ofPrepared();
        }
        if (klipAuthLoginResponse.isFailed()) {
            return UserLoginResponse.ofFailed();
        }
        return completeLogin(klipAuthLoginResponse.getKlaytnAddress());
    }

    private UserLoginSuccessResponse completeLogin(String klaytnAddress) {
        Optional<User> optionalUser = userRepository.findByKlaytnAddressAndIsActive(klaytnAddress, true);
        return optionalUser.map(user -> UserLoginSuccessResponse.of(
                user,
                jwtProvider.generateToken(klaytnAddress),
                false
        )).orElseGet(() -> completeNewUserLogin(klaytnAddress));
    }

    private UserLoginSuccessResponse completeNewUserLogin(String klaytnAddress) {
        User user = User.builder()
                .klaytnAddress(klaytnAddress)
                .isActive(true)
                .build();
        userRepository.save(user);
        return UserLoginSuccessResponse.of(
                user,
                jwtProvider.generateToken(klaytnAddress),
                true
        );
    }

    public UserResponse getUserByWalletAddress(String klaytnAddress) {
        Optional<User> foundUser = userRepository.findByKlaytnAddress(klaytnAddress);
        if (foundUser.isEmpty()) {
            throw new IllegalArgumentException("해당 지갑에 대응되는 사용자가 없습니다.");
        }
        User user = foundUser.get();
        return UserResponse.of(user);
    }

    public UserDeleteResponse deleteUserByKlaytnAddress(String klaytnAddress) {
        userRepository.deleteUser(klaytnAddress);
        return UserDeleteResponse.ofSuccess();
    }
}
