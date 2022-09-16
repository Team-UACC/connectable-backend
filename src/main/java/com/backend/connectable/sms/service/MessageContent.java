package com.backend.connectable.sms.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MessageContent {
    SIGNUP_AUTH_REQUEST("Connectable 인증번호는 %s 입니다."),
    TICKET_ORDER_SUCCREE("티켓이 발송완료되었습니다. 마이페이지 > 마이티켓에서 확인부탁드립니다.");

    private final String message;

    public String getAuthSmsMessage(String authKey) {
        return String.format(message, authKey);
    }
}
