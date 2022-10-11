package com.backend.connectable.security.custom;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ConnectableUserDetails extends CustomUserDetails {

    private final String klaytnAddress;
}
