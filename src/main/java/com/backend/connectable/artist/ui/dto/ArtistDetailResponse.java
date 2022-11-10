package com.backend.connectable.artist.ui.dto;

import com.backend.connectable.artist.domain.Artist;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ArtistDetailResponse {

    private Long id;
    private String name;
    private String image;
    private String twitterUrl;
    private String instagramUrl;
    private String webpageUrl;
    private String description;
    private NoticeResponse notice;

    public ArtistDetailResponse(Long id, String name, String image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public ArtistDetailResponse(
            Long id,
            String name,
            String image,
            String twitterUrl,
            String instagramUrl,
            String webpageUrl,
            String description,
            NoticeResponse notice) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.twitterUrl = twitterUrl;
        this.instagramUrl = instagramUrl;
        this.webpageUrl = webpageUrl;
        this.description = description;
        this.notice = notice;
    }

    public static ArtistDetailResponse from(Artist artist) {
        return new ArtistDetailResponse(
                artist.getId(), artist.getArtistName(), artist.getArtistImage());
    }

    public static List<ArtistDetailResponse> toList(List<Artist> artists) {
        return artists.stream().map(ArtistDetailResponse::from).collect(Collectors.toList());
    }
}
