package com.metalancer.backend.common.constants;

import java.util.Arrays;

import static com.metalancer.backend.common.constants.HttpStatus.*;

public enum ErrorCode {
    /**
     * 회원가입 및 로그인
     */
    SIGNUP_CREATED_OK(CREATED, "A001", "회원가입 성공"),
    SIGNUP_DUPLICATED(DUPLICATED_VALUE, "A002", "이미 존재하는 회원"),
    SIGNUP_DUPLICATED_ID(DUPLICATED_VALUE, "A003", "ID 중복"),
    SIGNUP_DUPLICATED_USERNAME(DUPLICATED_VALUE, "A004", "USERNAME 중복"),
    SIGNUP_FAILED(BAD_REQUEST, "A005", "회원가입 실패"),

    LOGIN_OK(SUCCESS, "B001", "로그인 성공"),
    LOGIN_NOT_FOUND_ID_PW(NOT_FOUND_VALUE, "B002", "아이디 존재하지않거나 비밀번호가 일치하지않습니다."),
    LOGOUT_OK(SUCCESS, "B003", "로그아웃 성공"),
    LOGOUT_STATE(UNAUTHORIZED, "B004", "로그아웃 상태"),
    LOGIN_DENIED(NOT_FOUND_VALUE, "B005", "로그인 불가"),
    LOGIN_REQUIRED(UNAUTHORIZED, "B006", "로그인 필요"),
    /**
     * 회원정보
     */
    USER_GET_OK(SUCCESS, "C001", "회원정보 있음"),
    USER_NOT_FOUND(NOT_FOUND_VALUE, "C002", "회원정보 없음"),
    NICKNAME_UPDATE_COUNT_PROHIBIT(BAD_REQUEST, "C003", "닉네임을 1번 변경한 적이 있습니다."),

    USER_UPDATE_OK(SUCCESS, "D001", "회원정보 수정 성공"),
    USER_UPDATE_INVALID(NOT_FOUND_VALUE, "D002", "회원정보 수정 실패"),
    USER_KAKAO_INVALID(NOT_FOUND_VALUE, "D003", "KAKAO 회원정보 조회 실패"),

    /**
     * 권한여부
     */
    AUTHORITY_HAVE(SUCCESS, "E001", "수정/삭제 권한이 있습니다"),
    AUTHORITY_NOT_HAVE(NOT_FOUND_VALUE, "E002", "수정/삭제 권한이 없습니다."),
    INVALID_ROLE_ACCESS(FORBIDDEN, "E003", "올바르지 않은 권한 접근입니다."),
    IS_NOT_WRITER(INVALID_ACCESS, "E004", "본인만 접근 가능합니다."),
    /**
     * 포트원 결제
     */
    PORTONE_ERROR(NOT_FOUND_VALUE, "F002", "포트원 api 호출에 실패했습니다."),
    ILLEGAL_ORDER_STATUS(INVALID_VALUE, "F003", "잘못된 주문 상태 입니다."),
    FAIL_TO_ORDER(INVALID_VALUE, "F004", "주문에 실패했습니다."),

    /**
     * 이미지
     */
    IMAGES_UPLOAD_FAILED(UNEXPECTED_ERROR, "G002", "이미지 업로드에 실패했습니다"),
    FILES_UPLOAD_FAILED(UNEXPECTED_ERROR, "G003", "파일 업로드에 실패했습니다"),
    ASSETS_UPLOAD_FAILED(UNEXPECTED_ERROR, "G004", "에셋 등록에 실패했습니다"),
    THUMBNAILS_UPLOAD_FAILED(UNEXPECTED_ERROR, "G005", "썸네일 등록에 실패했습니다"),
    VIEWS_UPLOAD_FAILED(UNEXPECTED_ERROR, "G006", "3D 뷰 등록에 실패했습니다"),
    SHORTEN_URL_FAILED(UNEXPECTED_ERROR, "G007", "presigned url 축약에 실패했습니다"),
    /**
     * 토큰
     */
    REISSUE_TOKEN(SUCCESS, "H001", "reissued token"),
    EXPIRED_TOKEN(INVALID_ACCESS, "F002", "expired access-token"),
    MALFORMED_TOKEN(INVALID_ACCESS, "F003", "incorrect access-token"),
    UNAUTHORIZED_TOKEN(INVALID_ACCESS, "F004", "invalid access-token"),

    INVALID_REFRESH_TOKEN(INVALID_ACCESS, "F005", "non-existent refresh-token"),

    /**
     * 상품
     */

    PRODUCTS_STATUS_ERROR(INVALID_VALUE, "H001", "상품이 삭제되었거나 제한되었습니다."),

    TYPE_NOT_FOUND(NOT_FOUND_VALUE, "Z002", "해당 type이 존재하지 않습니다."),

    INVALID_PARAMETER(BAD_REQUEST, "Z001", "요청값이 올바르지 않습니다."),
    NOT_FOUND(NOT_FOUND_VALUE, "Z002", "존재하지 않습니다."),
    ILLEGAL_DATA_STATUS(INVALID_VALUE, "Z003", "잘못된 데이터 상태 입니다."),
    SYSTEM_ERROR(UNEXPECTED_ERROR, "Z004", "일시적 오류가 발생했습니다. 잠시 후 다시 시도해주세요."),
    FAIL_TO_CREATE(INVALID_VALUE, "Z005", "등록에 실패했습니다."),
    FAIL_TO_UPDATE(INVALID_VALUE, "Z006", "수정에 실패했습니다."),

    STATUS_DELETED(INVALID_VALUE, "Z101", "삭제된 상태입니다."),
    STATUS_PENDING(INVALID_VALUE, "Z102", "승인대기 상태입니다."),
    STATUS_BANNED(INVALID_VALUE, "Z103", "정지된 상태입니다."),
    ROLE_INVALID(INVALID_VALUE, "Z104", "올바르지않은 권한입니다."),
    /**
     * 잘못된 ExceptionCode
     */
    EMPTY(null, "", "");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
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

    public static ErrorCode findExceptionCodeByCode(String code) {
        return Arrays.stream(ErrorCode.values())
                .filter(x -> x.getCode().equals(code))
                .findFirst()
                .orElse(EMPTY);
    }
}