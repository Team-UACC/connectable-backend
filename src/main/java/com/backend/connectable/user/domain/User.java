package com.backend.connectable.user.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @Builder
    public User(Long id, String klaytnAddress, String nickname, String phoneNumber, boolean privacyAgreement) {
        this.id = id;
        this.klaytnAddress = klaytnAddress;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.privacyAgreement = privacyAgreement;
    }
}
