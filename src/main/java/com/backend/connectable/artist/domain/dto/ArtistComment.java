package com.backend.connectable.artist.domain.dto;

import com.backend.connectable.event.domain.TicketMetadata;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ArtistComment {

    private Long id;
    private String nickname;
    private String klaytnAddress;
    private LocalDateTime createdDate;
    private String contents;
    private boolean isDeleted;

    private TicketMetadata ticketMetadata;

    @QueryProjection
    public ArtistComment(
            Long id,
            String nickname,
            String klaytnAddress,
            LocalDateTime createdDate,
            String contents,
            boolean isDeleted) {
        this.id = id;
        this.nickname = nickname;
        this.klaytnAddress = klaytnAddress;
        this.createdDate = createdDate;
        this.contents = contents;
        this.isDeleted = isDeleted;
    }

    public void addHoldingTicketMetadata(TicketMetadata ticketMetadata) {
        this.ticketMetadata = ticketMetadata;
    }
}
