package com.backend.connectable.fixture;

import com.backend.connectable.artist.domain.Artist;

public class ArtistFixture {

    private ArtistFixture() {}

    public static Artist createArtistBigNaughty() {
        return Artist.builder()
                .bankCompany("NH")
                .bankAccount("9000000000099")
                .artistName("빅나티")
                .email("bignaughty@gmail.com")
                .password("temptemp1234")
                .phoneNumber("01085161399")
                .artistImage("ARTIST_IMAGE")
                .instagramUrl("https://www.instagram.com/bignaughtyboi/")
                .webpageUrl("https://www.instagram.com/bignaughtyboi/")
                .description("목소리가 늑대같은 그남자")
                .build();
    }

    public static Artist createArtistChoi() {
        return Artist.builder()
                .bankCompany("TOSS")
                .bankAccount("7000000000077")
                .artistName("최유리")
                .email("choi777@naver.com")
                .password("temptemp1234")
                .phoneNumber("01033339999")
                .artistImage("https://image2.url")
                .instagramUrl("https://www.instagram.com/_choiyuree/")
                .webpageUrl("https://www.instagram.com/_choiyuree/")
                .description("목소리가 옥구슬 같은 그녀")
                .build();
    }
}
