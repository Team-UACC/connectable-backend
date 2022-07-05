package com.backend.connectable.user.service;

import com.backend.connectable.user.domain.User;
import com.backend.connectable.user.domain.UserRepository;
import com.backend.connectable.user.ui.dto.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        String klaytnAddress = "0x1234";
        String nickname = "Joel";
        String phoneNumber = "010-1234-5678";
        boolean privacyAgreement = true;

        user = User.builder()
                .klaytnAddress(klaytnAddress)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .privacyAgreement(privacyAgreement)
                .build();

        userRepository.save(user);
    }

    @DisplayName("클레이튼 지갑 주소로 특정 사용자를 조회할 수 있다.")
    @Test
    void getUserByWalletAddress() {
        // given & when
        UserResponse userResponse = userService.getUserByWalletAddress("0x1234");

        // then
        assertThat(userResponse.getNickname()).isEqualTo(user.getNickname());
        assertThat(userResponse.getKlaytnAddress()).isEqualTo(user.getKlaytnAddress());
        assertThat(userResponse.getPhoneNumber()).isEqualTo(user.getPhoneNumber());
    }
}