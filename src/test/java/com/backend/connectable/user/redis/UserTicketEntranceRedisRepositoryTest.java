package com.backend.connectable.user.redis;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class UserTicketEntranceRedisRepositoryTest {

    @Autowired private UserTicketEntranceRedisRepository userTicketEntranceRedisRepository;

    @DisplayName("KlaytnAddress를 id로 가진 UserTicketEntrance 객체를 저장한다.")
    @Test
    void saveUserTicketEntrance() {
        // given
        String klaytnAddress = "0x1234abcd";
        Long ticketId = 10L;
        String verification = "randomly-generated";

        // when
        UserTicketEntrance userTicketEntrance =
                UserTicketEntrance.builder()
                        .klaytnAddress(klaytnAddress)
                        .ticketId(ticketId)
                        .verification(verification)
                        .build();
        userTicketEntranceRedisRepository.save(userTicketEntrance);

        // then
        UserTicketEntrance savedUserTicketEntrance =
                userTicketEntranceRedisRepository.findById(klaytnAddress).get();
        assertThat(savedUserTicketEntrance.getKlaytnAddress()).isEqualTo(klaytnAddress);
        assertThat(savedUserTicketEntrance.getTicketId()).isEqualTo(ticketId);
        assertThat(savedUserTicketEntrance.getVerification()).isEqualTo(verification);
    }

    @DisplayName("KlaytnAddress가 같은 UserTicketEntrance가 저장된다면, UserTicketEntrance 정보가 바뀐다.")
    @Test
    void saveDuplicateInvalid() {
        // given
        String klaytnAddress = "0x1234abcd";

        UserTicketEntrance prevTicketEntrance =
                UserTicketEntrance.builder()
                        .klaytnAddress(klaytnAddress)
                        .ticketId(1L)
                        .verification("verification")
                        .build();
        userTicketEntranceRedisRepository.save(prevTicketEntrance);

        // when
        Long ticketId = 10L;
        String verification = "randomly-generated";
        UserTicketEntrance userTicketEntrance =
                UserTicketEntrance.builder()
                        .klaytnAddress(klaytnAddress)
                        .ticketId(ticketId)
                        .verification(verification)
                        .build();
        userTicketEntranceRedisRepository.save(userTicketEntrance);

        // then
        UserTicketEntrance savedUserTicketEntrance =
                userTicketEntranceRedisRepository.findById(klaytnAddress).get();
        assertThat(savedUserTicketEntrance.getKlaytnAddress()).isEqualTo(klaytnAddress);
        assertThat(savedUserTicketEntrance.getTicketId()).isEqualTo(ticketId);
        assertThat(savedUserTicketEntrance.getVerification()).isEqualTo(verification);
    }
}
