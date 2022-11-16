package com.backend.connectable.artist.domain.repository;

import com.backend.connectable.artist.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {}
