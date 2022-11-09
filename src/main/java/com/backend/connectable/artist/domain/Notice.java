package com.backend.connectable.artist.domain;

import com.backend.connectable.global.entity.BaseEntity;
import javax.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Artist artist;

    private NoticeStatus noticeStatus;

    private String title;

    @Lob private String contents;

    @Builder
    public Notice(
            Long id, Artist artist, NoticeStatus noticeStatus, String title, String contents) {
        this.id = id;
        this.artist = artist;
        this.noticeStatus = noticeStatus;
        this.title = title;
        this.contents = contents;
    }
}
