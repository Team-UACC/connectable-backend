package com.backend.connectable.artist.ui.dto;

import com.backend.connectable.artist.domain.dto.ArtistComment;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
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

    public static ArtistCommentResponse from(ArtistComment artistComment) {
        return new ArtistCommentResponse(
                artistComment.getNickname(),
                artistComment.getContents(),
                artistComment.getCreatedDate());
    }

    public static List<ArtistCommentResponse> toList(List<ArtistComment> artistComments) {
        Function<ArtistComment, ArtistCommentResponse> convert =
                (artistComment -> from(artistComment));
        return artistComments.stream().map(convert).collect(Collectors.toList());
    }
}
