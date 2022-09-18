package com.backend.connectable.sms.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MessageContent {
    SIGNUP_AUTH_REQUEST("Connectable 인증번호는 %s 입니다."),
    TICKET_ORDER_SUCCESS("티켓이 발송완료되었습니다. 마이페이지 > 마이티켓에서 확인부탁드립니다.");

    private final String message;

    public static String getSignUpAuthRequestMessage(String authKey) {
        return String.format(SIGNUP_AUTH_REQUEST.message, authKey);
    }

    public static String getTicketOrderSuccess() {
        return TICKET_ORDER_SUCCESS.message;
    }
}
