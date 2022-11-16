package com.backend.connectable.artist.domain.repository;

import com.backend.connectable.artist.domain.dto.ArtistComment;
import java.util.List;

public interface CommentRepositoryCustom {

    List<ArtistComment> getCommentsByArtistId(Long artistId);

    void deleteComment(Long artistId, Long commentId);
}
