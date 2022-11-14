package com.backend.connectable.artist.domain.repository;

import static com.backend.connectable.artist.domain.QArtist.artist;
import static com.backend.connectable.artist.domain.QNotice.notice;
import static com.backend.connectable.event.domain.QEvent.event;

import com.backend.connectable.artist.domain.NoticeStatus;
import com.backend.connectable.artist.domain.dto.ArtistDetail;
import com.backend.connectable.artist.domain.dto.QArtistDetail;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class ArtistRepositoryImpl implements ArtistRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public ArtistRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    public Optional<ArtistDetail> findArtistDetailByArtistId(Long artistId) {
        ArtistDetail result =
                queryFactory
                        .select(
                                new QArtistDetail(
                                        artist.id,
                                        artist.artistImage.as("image"),
                                        artist.artistName.as("name"),
                                        artist.twitterUrl,
                                        artist.instagramUrl,
                                        artist.webpageUrl,
                                        artist.description,
                                        notice))
                        .from(artist)
                        .leftJoin(notice)
                        .on(
                                notice.artist
                                        .id
                                        .eq(artist.id)
                                        .and(notice.noticeStatus.eq(NoticeStatus.EXPOSURE)))
                        .where(artist.id.eq(artistId))
                        .orderBy(notice.createdDate.desc())
                        .limit(1)
                        .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public List<String> findArtistEventsContractAddresses(Long artistId) {
        return queryFactory
                .select(event.contractAddress)
                .from(event)
                .innerJoin(artist)
                .on(event.artist.id.eq(artist.id))
                .where(event.artist.id.eq(artistId))
                .fetch();
    }
}
