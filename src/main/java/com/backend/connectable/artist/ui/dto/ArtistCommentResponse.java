package com.backend.connectable.artist.ui.dto;

import com.backend.connectable.event.domain.TicketMetadata;
import com.backend.connectable.event.ui.dto.TicketMetadataResponse;
import com.backend.connectable.global.util.DateTimeUtil;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ArtistCommentResponse {

    private String nickname;
    private String contents;
    private Long writtenAt;
    private TicketMetadataResponse ticketMetadata;

    @Builder
    public ArtistCommentResponse(
            String nickname,
            String contents,
            LocalDateTime writtenAt,
            TicketMetadata ticketMetadata) {
        this.nickname = nickname;
        this.contents = contents;
        this.writtenAt = DateTimeUtil.toEpochMilliSeconds(writtenAt);
        this.ticketMetadata = TicketMetadataResponse.of(ticketMetadata);
    }
}
