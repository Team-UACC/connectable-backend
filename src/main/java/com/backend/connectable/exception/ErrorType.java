package com.backend.connectable.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorType {
    SECURITY_EXCEPTION("SECURITY-001", "로그인이 필요합니다."),

    USER_NOT_FOUND("AUTH-001", "해당 유저를 찾을 수 없습니다."),
    INVALID_TOKEN("AUTH-002", "유효하지 않은 토큰입니다."),
    TOKEN_PAYLOAD_EXTRACTION_FAILURE("AUTH-003", "토큰 페이로드 추출에 실패했습니다"),
    INVALID_AUTH_KEY("AUTH-004", "올바르지 않은 인증키입니다."),
    NURIGO_EXCEPTION("AUTH-005", "서버 내 외부모듈 통신에러"),

    MISSING_REQUIRED_VALUE_ERROR("COMMON-001", "필수 요청값이 누락되었습니다."),
    NOT_ALLOWED_PERMISSION_ERROR("COMMON-002", "허용되지 않은 권한입니다."),
    DUPLICATED_REQUEST_ERROR("COMMON-003", "중복된 요청입니다."),
    INVALID_REQUEST_ERROR("COMMON-004", "올바르지 않은 데이터 요청입니다."),
    ASYNC_HANDLING_ERROR("COMMON-005", "비동기 처리에서 문제가 발생했습니다."),
    NETWORK_ERROR("COMMON-006", "네트워크 처리에서 문제가 발생했습니다."),

    NICKNAME_FORMAT_ERROR("USER-001", "올바르지 않은 닉네임 입력 양식입니다."),
    EMAIL_FORMAT_ERROR("USER-002", "올바르지 않은 이메일 입력 양식입니다."),
    DUPLICATED_NICKNAME_ERROR("USER-003", "중복된 닉네임입니다."),
    DUPLICATED_EMAIL_ERROR("USER-004", "중복된 이메일입니다."),

    SERVICE_BEING_CHECKED("SERVICE-001", "서비스가 점검중입니다."),

    EVENT_NOT_EXISTS("EVENT-001", "존재하지 않는 이벤트입니다."),

    TICKET_NOT_EXISTS("TICKET-001", "존재하지 않는 티켓입니다."),
    TICKET_TO_PENDING_UNAVAILABLE("TICKET-002", "ON SALE 상태만 PENDING으로 변할 수 있습니다."),
    TICKET_TO_SOLD_OUT_UNAVAILABLE("TICKET-003", "PENDING 상태만 SOLD_OUT으로 변할 수 있습니다."),
    TICKET_TO_ON_SALE_UNAVAILABLE("TICKET-004", "PENDING 상태만 ON_SALE로 변할 수 있습니다."),
    TICKET_METADATA_TO_JSON_FAILURE("TICKET-005", "티켓 메타데이터를 JSON으로 변환하는데 실패했습니다"),
    TICKET_JSON_TO_METADATA_FAILURE("TICKET-006", "JSON을 티켓 메타데이터로 변환하는데 실패했습니다"),
    TICKET_ALREADY_USED("TICKET-007", "이미 입장에 사용된 티켓입니다"),
    TICKET_METADATA_NOT_FOUND("TICKET-008", "티켓의 메타데이터가 존재하지 않습니다"),

    ORDER_TO_PAID_UNAVAILABLE("ORDER-001", "PAID 상태로 변경이 불가합니다."),
    ORDER_TO_UNPAID_UNAVAILABLE("ORDER-002", "UNPAID 상태로 변경이 불가합니다."),
    ORDER_TO_REFUND_UNAVAILABLE("ORDER-003", "REFUND 상태로 변경이 불가합니다."),
    ORDER_TO_TRANSFER_SUCCESS_UNAVAILABLE("ORDER-004", "TRANSFER SUCCESS 상태로 변경이 불가합니다."),
    ORDER_TO_TRANSFER_FAIL_UNAVAILABLE("ORDER-005", "TRANSFER FAIL 상태로 변경이 불가합니다."),

    ENTRANCE_ALREADY_DONE("ENTRANCE-001", "입장 처리가 완료된 티켓입니다."),
    ENTRANCE_AUTHORIZATION_NEEDED("ENTRANCE-002", "입장 처리에 대한 권한이 없습니다"),
    ENTRANCE_INFO_NOT_FOUND("ENTRANCE-003", "QR 정보가 만료되었거나 생성되지 않았습니다."),
    ENTRANCE_INFO_INVALID("ENTRANCE-004", "올바르지 않은 입장 정보입니다."),

    ORDER_DETAIL_NOT_EXISTS("ADMIN-001", "존재하지 않는 주문 상세입니다."),
    ADMIN_TOKEN_VERIFY_FAILURE("ADMIN-002", "어드민 토큰이 아닙니다."),
    ARTIST_NOT_FOUND("ADMIN-003", "해당 ID에 대응되는 아티스트가 없습니다."),
    EVENT_NOT_FOUND("ADMIN-004", "해당 ID에 대응되는 이벤트가 없습니다."),
    THREAD_INTERRUPTED("ADMIN-005", "쓰레드에 문제가 발생했습니다."),

    ARTIST_NOT_EXISTS("ARTIST-001", "존재하지 않는 아티스트입니다."),

    COMMENT_NOT_EXIST("COMMENT-001", "존재하지 않는 댓글입니다."),
    NOT_A_COMMENT_AUTHOR("COMMENT-002", "댓글 작성자가 아닙니다."),

    UNEXPECTED_SERVER_ERROR("SERVER-001", "서버 관리자에게 문의하세요.");

    private final String errorCode;
    private final String message;
}
