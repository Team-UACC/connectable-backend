package com.backend.connectable.event.domain.repository;

import com.backend.connectable.artist.domain.Artist;
import com.backend.connectable.artist.domain.repository.ArtistRepository;
import com.backend.connectable.event.domain.Event;
import com.backend.connectable.event.domain.EventSalesOption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@DataJpaTest
class EventRepositoryTest {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    ArtistRepository artistRepository;

    Artist bigNaughty = Artist.builder()
        .bankCompany("NH")
        .bankAccount("9000000000099")
        .artistName("빅나티")
        .email("bignaughty@gmail.com")
        .password("temptemp1234")
        .phoneNumber("01012345678")
        .artistImage("ARTIST_IMAGE")
        .build();

    String joelEventContractAddress = "0x1234";
    Event joelEvent = Event.builder()
        .description("조엘의 콘서트 at Connectable")
        .salesFrom(LocalDateTime.of(2022, 7, 12, 0, 0))
        .salesTo(LocalDateTime.of(2022, 7, 30, 0, 0))
        .contractAddress(joelEventContractAddress)
        .eventName("조엘의 콘서트")
        .eventImage("JOEL_EVENT_IMG_URL")
        .twitterUrl("https://github.com/joelonsw")
        .instagramUrl("https://www.instagram.com/jyoung_with/")
        .webpageUrl("https://papimon.tistory.com/")
        .startTime(LocalDateTime.of(2022, 8, 1, 18, 0))
        .endTime(LocalDateTime.of(2022, 8, 1, 19, 0))
        .eventSalesOption(EventSalesOption.FLAT_PRICE)
        .location("서울특별시 강남구 테헤란로 311 아남타워빌딩 7층")
        .artist(bigNaughty)
        .build();

    String ryanEventContractAddress = "0x5678";
    Event ryanEvent = Event.builder()
        .description("라이언 콘서트 at Connectable")
        .salesFrom(LocalDateTime.of(2022, 6, 11, 0, 0))
        .salesTo(LocalDateTime.of(2022, 6, 17, 0, 0))
        .contractAddress(ryanEventContractAddress)
        .eventName("라이언 콘서트")
        .eventImage("RYAN_EVENT_IMG_URL")
        .twitterUrl("https://twitter.com/elonmusk")
        .instagramUrl("https://www.instagram.com/eunbining0904/")
        .webpageUrl("https://nextjs.org/")
        .startTime(LocalDateTime.of(2022, 6, 22, 19, 30))
        .endTime(LocalDateTime.of(2022, 6, 22, 21, 30))
        .eventSalesOption(EventSalesOption.FLAT_PRICE)
        .location("예술의 전당")
        .artist(bigNaughty)
        .build();

    @BeforeEach
    void setUp() {
        eventRepository.deleteAll();
        artistRepository.deleteAll();

        artistRepository.save(bigNaughty);
        eventRepository.saveAll(Arrays.asList(joelEvent, ryanEvent));
    }


    @DisplayName("이벤트의 컨트랙트 주소를 받아올 수 있다.")
    @Test
    void findAllContractAddresses() {
        List<String> allContractAddresses = eventRepository.findAllContractAddresses();
        assertThat(allContractAddresses).contains(joelEventContractAddress, ryanEventContractAddress);
    }
}