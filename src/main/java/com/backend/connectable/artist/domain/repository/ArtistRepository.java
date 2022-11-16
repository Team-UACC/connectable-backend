package com.backend.connectable.artist.domain.repository;

import com.backend.connectable.artist.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long>, ArtistRepositoryCustom {}
