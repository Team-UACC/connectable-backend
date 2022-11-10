package com.backend.connectable.artist.domain;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bankCompany;

    private String bankAccount;

    private String artistName;

    private String email;

    private String password;

    private String phoneNumber;

    private String artistImage;

    private String twitterUrl;

    private String instagramUrl;

    private String webpageUrl;

    private String description;

    @Builder
    public Artist(
            Long id,
            String bankCompany,
            String bankAccount,
            String artistName,
            String email,
            String password,
            String phoneNumber,
            String artistImage,
            String description,
            String twitterUrl,
            String instagramUrl,
            String webpageUrl) {
        this.id = id;
        this.bankCompany = bankCompany;
        this.bankAccount = bankAccount;
        this.artistName = artistName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.artistImage = artistImage;
        this.description = description;
        this.twitterUrl = twitterUrl;
        this.instagramUrl = instagramUrl;
        this.webpageUrl = webpageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return Objects.equals(id, artist.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
