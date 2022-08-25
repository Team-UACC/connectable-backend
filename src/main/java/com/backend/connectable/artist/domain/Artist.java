package com.backend.connectable.artist.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

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

    @Builder
    public Artist(Long id, String bankCompany, String bankAccount, String artistName, String email, String password, String phoneNumber, String artistImage) {
        this.id = id;
        this.bankCompany = bankCompany;
        this.bankAccount = bankAccount;
        this.artistName = artistName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.artistImage = artistImage;
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
