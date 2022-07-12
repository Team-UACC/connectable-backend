package com.backend.connectable.event.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    private String description;

    private LocalDateTime salesFrom;

    private LocalDateTime salesTo;

    private String contractAddress;

    private String eventName;

    private String eventImage;

    private String twitterUrl;

    private String instagramUrl;

    private String webpageUrl;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private SalesOption salesOption;

    @Builder
    public Event(Long id, String description, LocalDateTime salesFrom, LocalDateTime salesTo, String contractAddress, String eventName, String eventImage, String twitterUrl, String instagramUrl, String webpageUrl, LocalDateTime startTime, LocalDateTime endTime, SalesOption salesOption) {
        this.id = id;
        this.description = description;
        this.salesFrom = salesFrom;
        this.salesTo = salesTo;
        this.contractAddress = contractAddress;
        this.eventName = eventName;
        this.eventImage = eventImage;
        this.twitterUrl = twitterUrl;
        this.instagramUrl = instagramUrl;
        this.webpageUrl = webpageUrl;
        this.startTime = startTime;
        this.endTime = endTime;
        this.salesOption = salesOption;
    }
}
