package com.backend.connectable.artist.domain.repository;

import com.backend.connectable.event.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
}
