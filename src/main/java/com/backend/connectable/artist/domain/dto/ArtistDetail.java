package com.backend.connectable.artist.domain.dto;

import com.backend.connectable.artist.domain.Notice;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ArtistDetail {

    private Long id;
    private String image;
    private String name;
    private String twitterUrl;
    private String instagramUrl;
    private String webpageUrl;
    private String description;
    private Notice notice;

    @QueryProjection
    public ArtistDetail(
            Long id,
            String image,
            String name,
            String twitterUrl,
            String instagramUrl,
            String webpageUrl,
            String description,
            Notice notice) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.twitterUrl = twitterUrl;
        this.instagramUrl = instagramUrl;
        this.webpageUrl = webpageUrl;
        this.description = description;
        this.notice = notice;
    }
}
