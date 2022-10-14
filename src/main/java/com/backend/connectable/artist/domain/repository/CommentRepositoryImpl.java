package com.backend.connectable.artist.domain.repository;

import static com.backend.connectable.artist.domain.QComment.comment;
import static com.backend.connectable.user.domain.QUser.user;

import com.backend.connectable.artist.domain.dto.ArtistComment;
import com.backend.connectable.artist.domain.dto.QArtistComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;

public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CommentRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<ArtistComment> getCommentsByArtistId(Long artistId) {
        List<ArtistComment> result =
                queryFactory
                        .select(
                                new QArtistComment(
                                        comment.id,
                                        user.nickname,
                                        comment.createdDate,
                                        comment.contents))
                        .from(comment)
                        .innerJoin(user)
                        .on(user.id.eq(comment.user.id))
                        .where(comment.artist.id.eq(artistId))
                        .fetch();

        return result;
    }
}
