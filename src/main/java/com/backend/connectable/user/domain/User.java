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

    @Column(name = "wallet_address")
    private String walletAddress;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "privacy_agreement")
    private boolean privacyAgreement;

    @Builder
    public User(Long id, String walletAddress, String nickname, String phoneNumber, boolean privacyAgreement) {
        this.id = id;
        this.walletAddress = walletAddress;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.privacyAgreement = privacyAgreement;
    }
}
