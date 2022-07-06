package com.backend.connectable.user.service;

import com.backend.connectable.klip.service.KlipService;
import com.backend.connectable.klip.service.dto.KlipAuthLoginResponse;
import com.backend.connectable.user.domain.User;
import com.backend.connectable.user.domain.UserRepository;
import com.backend.connectable.user.ui.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @MockBean
    KlipService klipService;

    private User user;
    private final String klaytnAddress = "0x1234";
    private final String nickname = "Joel";
    private final String phoneNumber = "010-1234-5678";
    private final boolean privacyAgreement = true;
    private final boolean isActive = true;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        user = User.builder()
                .klaytnAddress(klaytnAddress)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .privacyAgreement(privacyAgreement)
                .isActive(isActive)
                .build();

        userRepository.save(user);
    }

    @DisplayName("클레이튼 지갑 주소로 특정 사용자를 조회할 수 있다.")
    @Test
    void getUserByWalletAddress() {
        // given & when
        UserResponse userResponse = userService.getUserByWalletAddress(klaytnAddress);

        // then
        assertThat(userResponse.getNickname()).isEqualTo(user.getNickname());
        assertThat(userResponse.getKlaytnAddress()).isEqualTo(user.getKlaytnAddress());
        assertThat(userResponse.getPhoneNumber()).isEqualTo(user.getPhoneNumber());
    }

    @DisplayName("클레이튼 지갑 주소로 특정 사용자 회원탈퇴를 실행할 수 있다.")
    @Test
    void deleteUserByKlaytnAddress() {
        // given & when
        UserDeleteResponse userDeleteResponse = userService.deleteUserByKlaytnAddress(klaytnAddress);

        // then
        assertThat(userDeleteResponse.getStatus()).isEqualTo("success");
    }

    @DisplayName("Klip에서 로그인이 completed 되었고, 이미 가입된 회원이라면 completed, klaytnAddress, jwt, isNew=False 를 받게된다")
    @Test
    void loginExistingUser() {
        // given
        KlipAuthLoginResponse klipAuthLoginResponse = KlipAuthLoginResponse.ofCompleted(klaytnAddress);
        given(klipService.authLogin("properKey")).willReturn(klipAuthLoginResponse);

        // when
        UserLoginResponse userLoginResponse = userService.login(new UserLoginRequest("properKey"));
        UserLoginSuccessResponse userLoginSuccessResponse = (UserLoginSuccessResponse) userLoginResponse;

        // then
        assertThat(userLoginSuccessResponse.getStatus()).isEqualTo("completed");
        assertThat(userLoginSuccessResponse.getKlaytnAddress()).isEqualTo(klaytnAddress);
        assertThat(userLoginSuccessResponse.getIsNew()).isFalse();
        assertThat(userLoginSuccessResponse.getJwt()).isNotEmpty();
    }

    @DisplayName("Klip에서 로그인이 completed 되었고, 새로 가입한 회원이라면 completed, klaytnAddress, jwt, isNew=True 를 받게된다")
    @Test
    void loginNewUser() {
        // given
        String newKlaytnAddress = "0x9dsf8xc12x9c0v";
        KlipAuthLoginResponse klipAuthLoginResponse = KlipAuthLoginResponse.ofCompleted(newKlaytnAddress);
        given(klipService.authLogin("properKey")).willReturn(klipAuthLoginResponse);

        // when
        UserLoginResponse userLoginResponse = userService.login(new UserLoginRequest("properKey"));
        UserLoginSuccessResponse userLoginSuccessResponse = (UserLoginSuccessResponse) userLoginResponse;

        // then
        assertThat(userLoginSuccessResponse.getStatus()).isEqualTo("completed");
        assertThat(userLoginSuccessResponse.getKlaytnAddress()).isEqualTo(newKlaytnAddress);
        assertThat(userLoginSuccessResponse.getIsNew()).isTrue();
        assertThat(userLoginSuccessResponse.getJwt()).isNotEmpty();
    }


    @DisplayName("Klip에서 로그인이 prepared 라면, status=prepared 를 받게된다")
    @Test
    void loginPrepared() {
        // given
        KlipAuthLoginResponse klipAuthLoginResponse = KlipAuthLoginResponse.ofPrepared();
        given(klipService.authLogin("properKey")).willReturn(klipAuthLoginResponse);

        // when
        UserLoginResponse userLoginResponse = userService.login(new UserLoginRequest("properKey"));

        // then
        assertThat(userLoginResponse.getStatus()).isEqualTo("prepared");
    }

    @DisplayName("Klip에서 로그인이 prepared 라면, status=failed 를 받게된다")
    @Test
    void loginFailed() {
        // given
        KlipAuthLoginResponse klipAuthLoginResponse = KlipAuthLoginResponse.ofFailed();
        given(klipService.authLogin("properKey")).willReturn(klipAuthLoginResponse);

        // when
        UserLoginResponse userLoginResponse = userService.login(new UserLoginRequest("properKey"));

        // then
        assertThat(userLoginResponse.getStatus()).isEqualTo("failed");
    }
}
