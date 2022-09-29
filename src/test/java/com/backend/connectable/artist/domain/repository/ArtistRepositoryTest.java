package com.backend.connectable.artist.domain.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.backend.connectable.artist.domain.Artist;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ArtistRepositoryTest {

    @Autowired ArtistRepository artistRepository;

    @BeforeEach
    void setUp() {
        artistRepository.deleteAll();
    }

    @DisplayName("이미 DB에 저장되어 있는 ID를 가진 아티스트를 저장하면, 해당 ID의 아티스트는 후에 작성된 아티스트 정보로 업데이트 된다.")
    @Test
    void saveSameId() {
        // given
        Artist artist1 = Artist.builder().artistName("artist1").build();
        artistRepository.save(artist1);

        // when
        Artist artist2 = Artist.builder().id(artist1.getId()).artistName("artist2").build();
        artistRepository.save(artist2);

        // then
        List<Artist> savedArtists = artistRepository.findAll();
        assertThat(savedArtists.size()).isEqualTo(1);

        Artist foundArtist = artistRepository.findById(artist1.getId()).get();
        assertThat(foundArtist.getArtistName()).isEqualTo("artist2");
    }

    @DisplayName("id가 없는 Artist 엔티티를 저장하면 순차적으로 ID를 부여하여 저장한다.")
    @Test
    void saveNoIdArtist() {
        // given
        Artist artist1 = Artist.builder().artistName("artist3").build();
        Artist artist2 = Artist.builder().artistName("artist4").build();

        // when
        artistRepository.save(artist1);
        artistRepository.save(artist2);

        // then
        long idDiff = artist2.getId() - artist1.getId();
        assertThat(idDiff).isEqualTo(1L);
    }
}
