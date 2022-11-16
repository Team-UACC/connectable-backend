package com.backend.connectable.artist.domain;

import lombok.Getter;

@Getter
public enum NoticeStatus {
    EXPOSURE("exposure"),
    NON_EXPOSURE("non_exposure");

    private final String status;

    NoticeStatus(String status) {
        this.status = status;
    }
}
