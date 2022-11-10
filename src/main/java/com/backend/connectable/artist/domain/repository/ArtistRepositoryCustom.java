package com.backend.connectable.artist.domain.repository;

import com.backend.connectable.artist.domain.dto.ArtistDetail;
import java.util.Optional;

public interface ArtistRepositoryCustom {

    Optional<ArtistDetail> findArtistDetailByArtistId(Long artistId);
}
