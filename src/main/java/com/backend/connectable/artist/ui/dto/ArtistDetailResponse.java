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

    private String artistName;
    private String artistImage;

    public ArtistDetailResponse(String artistName, String artistImage) {
        this.artistName = artistName;
        this.artistImage = artistImage;
    }

    public static ArtistDetailResponse from(Artist artist) {
        return new ArtistDetailResponse(artist.getArtistName(), artist.getArtistImage());
    }

    public static List<ArtistDetailResponse> toList(List<Artist> artists) {
        return artists.stream().map(ArtistDetailResponse::from).collect(Collectors.toList());
    }
}
