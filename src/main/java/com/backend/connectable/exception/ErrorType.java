package com.backend.connectable.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {
    MISSING_REQUIRED_VALUE_ERROR("COMMON-001", "필수 요청값이 누락되었습니다."),
    NOT_ALLOWED_PERMISSION_ERROR("COMMON-002", "허용되지 않은 권한입니다."),
    DUPLICATED_REQUEST_ERROR("COMMON-003", "중복된 요청입니다."),
    INVALID_REQUEST_ERROR("COMMON-004", "올바르지 않은 데이터 요청입니다."),

    NICKNAME_FORMAT_ERROR("USER-001", "올바르지 않은 닉네임 입력 양식입니다."),
    EMAIL_FORMAT_ERROR("USER-002", "올바르지 않은 이메일 입력 양식입니다."),
    DUPLICATED_NICKNAME_ERROR("USER-003", "중복된 닉네임입니다."),
    DUPLICATED_EMAIL_ERROR("USER-004", "중복된 이메일입니다."),

    SERVICE_BEING_CHECKED("SERVICE-001", "서비스가 점검중입니다.")
    ;

    private final String errorCode;
    private final String message;
}
