package com.backend.connectable.artist.ui.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ArtistCommentResponse {

    private String nickname;
    private String contents;
    private LocalDateTime writtenAt;

    public ArtistCommentResponse(String nickname, String contents, LocalDateTime writtenAt) {
        this.nickname = nickname;
        this.contents = contents;
        this.writtenAt = writtenAt;
    }
}
