package com.backend.connectable.artist.service;

import com.backend.connectable.artist.domain.Artist;
import com.backend.connectable.artist.domain.repository.ArtistRepository;
import com.backend.connectable.artist.ui.dto.ArtistDetailResponse;
import com.backend.connectable.exception.ConnectableException;
import com.backend.connectable.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ArtistService {

    private final ArtistRepository artistRepository;

    public ArtistDetailResponse getArtistDetail(Long artistId) {
        Artist artist = artistRepository.findById(artistId).orElseThrow(() -> new ConnectableException(
                HttpStatus.BAD_REQUEST,
                ErrorType.ARTIST_NOT_EXISTS)
        );

        return ArtistDetailResponse.of(artist);
    }
}
