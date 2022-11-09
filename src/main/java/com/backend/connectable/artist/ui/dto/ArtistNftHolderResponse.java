package com.backend.connectable.artist.ui.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ArtistNftHolderResponse {

    private boolean isNftHolder;

    public static ArtistNftHolderResponse isHolder() {
        return new ArtistNftHolderResponse(true);
    }

    public static ArtistNftHolderResponse isNotHolder() {
        return new ArtistNftHolderResponse(false);
    }

    public boolean getIsNftHolder() {
        return isNftHolder;
    }
}
