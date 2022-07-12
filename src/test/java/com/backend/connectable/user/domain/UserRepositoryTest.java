package com.backend.connectable.user.domain;

import com.backend.connectable.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager em;

    @BeforeEach
    void setUp() {
        String klaytnAddress = "0x1234";
        String nickname = "Joel";
        String phoneNumber = "010-1234-5678";
        boolean privacyAgreement = true;
        boolean isActive = true;

        User user = User.builder()
                .klaytnAddress(klaytnAddress)
                .nickname(nickname)
                .phoneNumber(phoneNumber)
                .privacyAgreement(privacyAgreement)
                .isActive(isActive)
                .build();

        userRepository.save(user);
    }

    @DisplayName("클레이튼 주소로 유저를 찾을수 있다.")
    @Test
    void findByKlaytnAddress() {
        // given & when
        Optional<User> optionalUser = userRepository.findByKlaytnAddress("0x1234");
        User user = optionalUser.get();

        // then
        assertThat(user.getNickname()).isEqualTo("Joel");
    }

    @DisplayName("클레이튼 주소로 유저를 삭제할 수 있다.")
    @Test
    void deleteByKlaytnAddress() {
        // given & when
        userRepository.deleteUser("0x1234");

        em.flush();
        em.clear();

        Optional<User> optionalUser = userRepository.findByKlaytnAddress("0x1234");
        User user = optionalUser.get();

        // then
        assertThat(user.isActive()).isFalse();
    }
}