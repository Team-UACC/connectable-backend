package com.backend.connectable.fixture;

import com.backend.connectable.artist.domain.Artist;
import com.backend.connectable.event.domain.Event;
import com.backend.connectable.event.domain.SalesOption;
import com.backend.connectable.kas.service.mockserver.KasMockRequest;
import java.time.LocalDateTime;

public class EventFixture {

    private EventFixture() {}

    public static Event createEvent(Artist artist) {
        return Event.builder()
                .description("콘서트 at Connectable")
                .salesFrom(LocalDateTime.of(2022, 7, 12, 0, 0))
                .salesTo(LocalDateTime.of(2022, 7, 30, 0, 0))
                .contractAddress("0x5555aaaa")
                .eventName("콘서트")
                .eventImage("EVENT_IMG_URL")
                .twitterUrl("https://github.com/joelonsw")
                .instagramUrl("https://www.instagram.com/jyoung_with/")
                .webpageUrl("https://papimon.tistory.com/")
                .startTime(LocalDateTime.of(2022, 8, 1, 18, 0))
                .endTime(LocalDateTime.of(2022, 8, 1, 19, 0))
                .salesOption(SalesOption.FLAT_PRICE)
                .location("서울특별시 강남구 테헤란로 311 아남타워빌딩 7층")
                .artist(artist)
                .build();
    }

    public static Event createEventValidContractAddressMockedKas(Artist artist) {
        return Event.builder()
                .description("콘서트 at Connectable")
                .salesFrom(LocalDateTime.of(2022, 7, 12, 0, 0))
                .salesTo(LocalDateTime.of(2022, 7, 30, 0, 0))
                .contractAddress(KasMockRequest.VALID_CONTRACT_ADDRESS)
                .eventName("콘서트")
                .eventImage("EVENT_IMG_URL")
                .twitterUrl("https://github.com/joelonsw")
                .instagramUrl("https://www.instagram.com/jyoung_with/")
                .webpageUrl("https://papimon.tistory.com/")
                .startTime(LocalDateTime.of(2022, 8, 1, 18, 0))
                .endTime(LocalDateTime.of(2022, 8, 1, 19, 0))
                .salesOption(SalesOption.FLAT_PRICE)
                .location("서울특별시 강남구 테헤란로 311 아남타워빌딩 7층")
                .artist(artist)
                .build();
    }
}
