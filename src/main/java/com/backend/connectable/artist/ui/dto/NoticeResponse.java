package com.backend.connectable.artist.ui.dto;

import com.backend.connectable.artist.domain.NoticeStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NoticeResponse {

    private String title;
    private String contents;
    private NoticeStatus noticeStatus;

    public NoticeResponse(String title, String contents, NoticeStatus noticeStatus) {
        this.title = title;
        this.contents = contents;
        this.noticeStatus = noticeStatus;
    }
}
