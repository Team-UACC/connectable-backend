package com.backend.connectable.user.service;

import com.backend.connectable.user.domain.User;
import com.backend.connectable.user.domain.UserRepository;
import com.backend.connectable.user.ui.dto.UserDeleteResponse;
import com.backend.connectable.user.ui.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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
