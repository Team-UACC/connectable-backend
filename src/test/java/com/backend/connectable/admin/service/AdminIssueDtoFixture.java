package com.backend.connectable.admin.service;

import com.backend.connectable.admin.ui.dto.EventIssueRequest;
import com.backend.connectable.artist.domain.Artist;
import java.time.LocalDateTime;

public class AdminIssueDtoFixture {

    private AdminIssueDtoFixture() {}

    public static EventIssueRequest eventIssueRequest(Artist artist) {
        return new EventIssueRequest(
                "contract",
                "CON",
                "con-tract",
                "eventName",
                "eventDescription",
                "eventImage",
                "eventTwitterUrl",
                "eventInstagramUrl",
                "eventWebpageUrl",
                "eventLocation",
                artist.getId(),
                LocalDateTime.of(2000, 1, 1, 0, 0, 0),
                LocalDateTime.of(2000, 1, 1, 1, 0, 0),
                LocalDateTime.of(2000, 1, 1, 2, 0, 0),
                LocalDateTime.of(2000, 1, 1, 3, 0, 0));
    }
}
