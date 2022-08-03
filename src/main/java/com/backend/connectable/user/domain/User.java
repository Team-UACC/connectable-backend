package com.backend.connectable.user.domain;

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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String klaytnAddress;

    private String nickname;

    private String phoneNumber;

    private boolean privacyAgreement;

    private boolean isActive;

    @Builder
    public User(Long id, String klaytnAddress, String nickname, String phoneNumber, boolean privacyAgreement, boolean isActive) {
        this.id = id;
        this.klaytnAddress = klaytnAddress.toLowerCase();
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.privacyAgreement = privacyAgreement;
        this.isActive = isActive;
    }

    public void modifyNickname(String nickname) {
        this.nickname = nickname;
    }

    public void modifyPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean hasNickname() {
        return !Objects.isNull(nickname);
    }

    public boolean hasPhoneNumber() {
        return !Objects.isNull(phoneNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
