package com.backend.connectable.artist.domain.repository;

import com.backend.connectable.artist.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {}
