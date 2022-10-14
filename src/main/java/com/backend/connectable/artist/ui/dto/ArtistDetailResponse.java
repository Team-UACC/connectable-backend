package com.backend.connectable.artist.ui.dto;

import com.backend.connectable.artist.domain.Artist;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ArtistDetailResponse {

    private String artistName;
    private String artistImage;

    @Builder
    public ArtistDetailResponse(String artistName, String artistImage) {
        this.artistName = artistName;
        this.artistImage = artistImage;
    }

    public static ArtistDetailResponse of(Artist artist) {
        return new ArtistDetailResponse(
            artist.getArtistName(),
            artist.getArtistImage()
        );
    }
}
