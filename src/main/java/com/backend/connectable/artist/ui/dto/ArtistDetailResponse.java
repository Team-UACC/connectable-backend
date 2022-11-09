package com.backend.connectable.artist.ui.dto;

import com.backend.connectable.artist.domain.Artist;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class ArtistDetailResponse {

    private Long artistId;
    private String artistName;
    private String artistImage;

    public ArtistDetailResponse(Long artistId, String artistName, String artistImage) {
        this.artistId = artistId;
        this.artistName = artistName;
        this.artistImage = artistImage;
    }

    public static ArtistDetailResponse from(Artist artist) {
        return new ArtistDetailResponse(
                artist.getId(), artist.getArtistName(), artist.getArtistImage());
    }

    public static List<ArtistDetailResponse> toList(List<Artist> artists) {
        return artists.stream().map(ArtistDetailResponse::from).collect(Collectors.toList());
    }
}
