package com.backend.connectable.event.domain;

import com.backend.connectable.artist.domain.Artist;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Artist artist;

    @Lob
    @Column(nullable = false)
    private String description;

    private LocalDateTime salesFrom;

    private LocalDateTime salesTo;

    private String contractAddress;

    private String contractName;

    private String eventName;

    private String location;

    private String eventImage;

    private String twitterUrl;

    private String instagramUrl;

    private String webpageUrl;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    private SalesOption salesOption;

    @Builder
    public Event(
            Long id,
            Artist artist,
            String description,
            LocalDateTime salesFrom,
            LocalDateTime salesTo,
            String contractAddress,
            String contractName,
            String eventName,
            String location,
            String eventImage,
            String twitterUrl,
            String instagramUrl,
            String webpageUrl,
            LocalDateTime startTime,
            LocalDateTime endTime,
            SalesOption salesOption) {
        this.id = id;
        this.artist = artist;
        this.description = description;
        this.salesFrom = salesFrom;
        this.salesTo = salesTo;
        this.contractAddress = contractAddress;
        this.contractName = contractName;
        this.eventName = eventName;
        this.location = location;
        this.eventImage = eventImage;
        this.twitterUrl = twitterUrl;
        this.instagramUrl = instagramUrl;
        this.webpageUrl = webpageUrl;
        this.startTime = startTime;
        this.endTime = endTime;
        this.salesOption = salesOption;
    }

    public String getArtistName() {
        return this.artist.getArtistName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
