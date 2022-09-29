package com.backend.connectable.artist.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ArtistTest {

    @DisplayName("아티스트를 빌더 패턴을 활용하여 생성할 수 있다")
    @Test
    void createArtist() {
        // given
        Long id = 1L;
        String bankCompany = "KB";
        String bankAccount = "251802-05-122323";
        String artistName = "name";
        String email = "email";
        String password = "pw";
        String phoneNumber = "010-1234-5678";
        String artistImage = "https://image.url";

        // when
        Artist artist =
                Artist.builder()
                        .id(id)
                        .bankCompany(bankCompany)
                        .bankAccount(bankAccount)
                        .artistName(artistName)
                        .email(email)
                        .password(password)
                        .phoneNumber(phoneNumber)
                        .artistImage(artistImage)
                        .build();

        // then
        assertThat(artist.getId()).isEqualTo(id);
        assertThat(artist.getBankCompany()).isEqualTo(bankCompany);
        assertThat(artist.getBankAccount()).isEqualTo(bankAccount);
        assertThat(artist.getArtistName()).isEqualTo(artistName);
        assertThat(artist.getEmail()).isEqualTo(email);
        assertThat(artist.getPassword()).isEqualTo(password);
        assertThat(artist.getPhoneNumber()).isEqualTo(phoneNumber);
        assertThat(artist.getArtistImage()).isEqualTo(artistImage);
    }

    @DisplayName("아티스트 도메인의 hashCode/equals는 id 값이 같다면 같은 것으로 처리된다.")
    @Test
    void artistEqualsById() {
        // given
        Long id = 1L;

        // when
        Artist artist1 = Artist.builder().id(1L).artistName("artist1").build();
        Artist artist2 = Artist.builder().id(1L).artistName("artist2").build();
        Set<Artist> artistSet = new HashSet<>();
        artistSet.addAll(Arrays.asList(artist1, artist2));

        // then
        assertThat(artist1).isEqualTo(artist2);
        assertThat(artist1.hashCode()).isEqualTo(artist2.hashCode());
        assertThat(artistSet.size()).isEqualTo(1);
    }
}
