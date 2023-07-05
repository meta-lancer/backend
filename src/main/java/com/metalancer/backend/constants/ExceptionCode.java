package com.metalancer.backend.constants;

import java.util.Arrays;

import static com.metalancer.backend.constants.HttpStatus.*;

public enum ExceptionCode {
    /**
     * 회원가입 및 로그인
     */
    SIGNUP_CREATED_OK(CREATED, "A001", "회원가입 성공"),
    SIGNUP_COMPLETE(CREATED, "A002", "이미 존재하는 회원"),
    SIGNUP_DUPLICATED_ID(DUPLICATED_VALUE, "A003", "ID 중복"),
    SIGNUP_DUPLICATED_USERNAME(DUPLICATED_VALUE, "A004", "USERNAME 중복"),

    LOGIN_OK(SUCCESS, "B001", "로그인 성공"),
    LOGIN_NOT_FOUND_ID(NOT_FOUND_VALUE, "B002", "로그인 실패"),
    LOGIN_NOT_FOUND_PW(NOT_FOUND_VALUE, "B003", "로그인 실패"),
    LOGOUT_OK(SUCCESS, "B004", "로그아웃 성공"),
    LOGOUT_STATE(UNAUTHORIZED, "B005", "로그아웃 상태"),

    /**
     * 회원정보
     */
    USER_GET_OK(SUCCESS, "C001", "회원정보 있음"),
    USER_NOT_FOUND(NOT_FOUND_VALUE, "C002", "회원정보 없음"),

    USER_UPDATE_OK(SUCCESS, "D001", "회원정보 수정 성공"),
    USER_UPDATE_INVALID(NOT_FOUND_VALUE, "D002", "회원정보 수정 실패"),
    USER_KAKAO_INVALID(NOT_FOUND_VALUE, "D003", "KAKAO 회원정보 조회 실패"),

    /**
     * 권한여부
     */
    AUTHORITY_HAVE(SUCCESS, "E001", "수정/삭제 권한이 있습니다"),
    AUTHORITY_NOT_HAVE(NOT_FOUND_VALUE, "E002", "수정/삭제 권한이 없습니다."),

    /**
     *  토큰
     */
    REISSUE_TOKEN(SUCCESS, "H001", "reissued token"),
    EXPIRED_TOKEN(INVALID_ACCESS, "F002","expired access-token"),
    MALFORMED_TOKEN(INVALID_ACCESS, "F003","incorrect access-token"),
    UNAUTHORIZED_TOKEN(INVALID_ACCESS, "F004", "invalid access-token"),

    INVALID_REFRESH_TOKEN(INVALID_ACCESS, "H005", "non-existent refresh-token"),

    /**
     * 잘못된 ExceptionCode
     */
    EMPTY(null, "", "");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ExceptionCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public static ExceptionCode findExceptionCodeByCode(String code) {
        return Arrays.stream(ExceptionCode.values())
                .filter(x -> x.getCode().equals(code))
                .findFirst()
                .orElse(EMPTY);
    }
}