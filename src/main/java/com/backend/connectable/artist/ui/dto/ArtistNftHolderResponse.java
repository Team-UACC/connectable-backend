package com.backend.connectable.artist.ui.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtistNftHolderResponse {
    private boolean isNftHolder;

    public static ArtistNftHolderResponse isHolder() {
        return new ArtistNftHolderResponse(true);
    }

    public static ArtistNftHolderResponse isNotHolder() {
        return new ArtistNftHolderResponse(false);
    }
}
