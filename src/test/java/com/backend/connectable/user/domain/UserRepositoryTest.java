package com.backend.connectable.user.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        String klaytnAddress = "0x1234";
        String nickname = "Joel";
        String phoneNumber = "010-1234-5678";
        boolean privacyAgreement = true;

        User user = User.builder()
                .klaytnAddress(klaytnAddress)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .privacyAgreement(privacyAgreement)
                .build();

        userRepository.save(user);
    }

    @Test
    void findByKlaytnAddress() {
        // given & when
        Optional<User> optionalUser = userRepository.findByKlaytnAddress("0x1234");
        User user = optionalUser.get();

        // then
        assertThat(user.getNickname()).isEqualTo("Joel");
    }
}