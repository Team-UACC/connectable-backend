package com.backend.connectable.fixture;

import com.backend.connectable.artist.domain.Artist;

public class ArtistFixture {

    private ArtistFixture() {
    }

    public static Artist createArtistBigNaughty() {
        return Artist.builder()
            .bankCompany("NH")
            .bankAccount("9000000000099")
            .artistName("빅나티")
            .email("bignaughty@gmail.com")
            .password("temptemp1234")
            .phoneNumber("01085161399")
            .artistImage("ARTIST_IMAGE")
            .build();
    }
}
