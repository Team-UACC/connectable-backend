package com.backend.connectable.user.service;

import com.backend.connectable.klip.service.KlipService;
import com.backend.connectable.klip.service.dto.KlipAuthLoginResponse;
import com.backend.connectable.security.ConnectableUserDetails;
import com.backend.connectable.user.domain.User;
import com.backend.connectable.user.domain.UserRepository;
import com.backend.connectable.user.ui.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    EntityManager em;

    @MockBean
    KlipService klipService;

    private User user1;
    private final String user1KlaytnAddress = "0x1234";
    private final String user1Nickname = "Joel";
    private final String user1PhoneNumber = "010-1234-5678";
    private final boolean user1PrivacyAgreement = true;
    private final boolean user1IsActive = true;


    private User noNicknameUser;
    private final String noNicknameUserKlaytnAddress = "0x8876";

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        user1 = User.builder()
                .klaytnAddress(user1KlaytnAddress)
                .nickname(user1Nickname)
                .phoneNumber(user1PhoneNumber)
                .privacyAgreement(user1PrivacyAgreement)
                .isActive(user1IsActive)
                .build();

        noNicknameUser = User.builder()
                        .klaytnAddress(noNicknameUserKlaytnAddress)
                        .nickname("")
                        .phoneNumber("010-0939-8822")
                        .privacyAgreement(true)
                        .isActive(true)
                        .build();

        userRepository.save(user1);
    }

    @DisplayName("ConnectableUserDetails로 특정 사용자를 조회할 수 있다.")
    @Test
    void getUserByUserDetails() {
        // given
        ConnectableUserDetails connectableUserDetails = new ConnectableUserDetails(user1);

        // when
        UserResponse userResponse = userService.getUserByUserDetails(connectableUserDetails);

        // then
        assertThat(userResponse.getNickname()).isEqualTo(user1.getNickname());
        assertThat(userResponse.getKlaytnAddress()).isEqualTo(user1.getKlaytnAddress());
        assertThat(userResponse.getPhoneNumber()).isEqualTo(user1.getPhoneNumber());
    }

    @DisplayName("ConnectableUserDetails로 특정 사용자 회원탈퇴를 실행할 수 있다.")
    @Test
    void deleteUser() {
        // given
        ConnectableUserDetails connectableUserDetails = new ConnectableUserDetails(user1);

        // when
        UserModifyResponse userModifyResponse = userService.deleteUserByUserDetails(connectableUserDetails);

        // then
        assertThat(userModifyResponse.getStatus()).isEqualTo("success");
    }
    
    @DisplayName("ConnectableUserDetails로 특정 사용자 수정을 실행할 수 있다.")
    @Test
    @Transactional
    void modifyUser() {
        // given
        ConnectableUserDetails connectableUserDetails = new ConnectableUserDetails(user1);
        UserModifyRequest userModifyRequest = new UserModifyRequest("mrlee7", "01085161399");

        // when
        UserModifyResponse userModifyResponse = userService.modifyUserByUserDetails(connectableUserDetails, userModifyRequest);
        em.flush();
        em.clear();

        // then
        assertThat(userModifyResponse.getStatus()).isEqualTo("success");
        User user = userRepository.getReferenceById(user1.getId());
        assertThat(user.getNickname()).isEqualTo("mrlee7");
        assertThat(user.getPhoneNumber()).isEqualTo("01085161399");
    }

    @DisplayName("Klip에서 로그인이 completed 되었고, 이미 가입된 회원이라면 completed, klaytnAddress, jwt, isNew=False 를 받게된다")
    @Test
    void loginExistingUser() {
        // given
        KlipAuthLoginResponse klipAuthLoginResponse = KlipAuthLoginResponse.ofCompleted(user1KlaytnAddress);
        given(klipService.authLogin("properKey")).willReturn(klipAuthLoginResponse);

        // when
        UserLoginResponse userLoginResponse = userService.login(new UserLoginRequest("properKey"));
        UserLoginSuccessResponse userLoginSuccessResponse = (UserLoginSuccessResponse) userLoginResponse;

        // then
        assertThat(userLoginSuccessResponse.getStatus()).isEqualTo("completed");
        assertThat(userLoginSuccessResponse.getKlaytnAddress()).isEqualTo(user1KlaytnAddress);
        assertThat(userLoginSuccessResponse.getIsNew()).isFalse();
        assertThat(userLoginSuccessResponse.getJwt()).isNotEmpty();
    }

    @DisplayName("Klip에서 로그인이 completed 되었지만, 등록만 된 회원이라면 completed, klaytnAddress, jwt, isNew=True 를 받게된다")
    @Test
    void loginOnlyRegisteredUser() {
        // given
        KlipAuthLoginResponse klipAuthLoginResponse = KlipAuthLoginResponse.ofCompleted(noNicknameUserKlaytnAddress);
        given(klipService.authLogin("properKey")).willReturn(klipAuthLoginResponse);

        // when
        UserLoginResponse userLoginResponse = userService.login(new UserLoginRequest("properKey"));
        UserLoginSuccessResponse userLoginSuccessResponse = (UserLoginSuccessResponse) userLoginResponse;

        // then
        assertThat(userLoginSuccessResponse.getStatus()).isEqualTo("completed");
        assertThat(userLoginSuccessResponse.getKlaytnAddress()).isEqualTo(noNicknameUserKlaytnAddress);
        assertThat(userLoginSuccessResponse.getIsNew()).isTrue();
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
