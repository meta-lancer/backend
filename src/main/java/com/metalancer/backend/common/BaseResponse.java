package com.metalancer.backend.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.metalancer.backend.common.constants.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"code", "message", "result"})
public class BaseResponse<T> {

    private final String message;
    private final String code;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    public BaseResponse() {
        this.message = "성공했습니다.";
        this.code = "A000";
    }

    public BaseResponse(T result) {
        this.message = "성공했습니다.";
        this.code = "A000";
        this.result = result;
    }

    public BaseResponse(ErrorCode status) {
        this.message = status.getMessage();
        this.code = status.getCode();
    }

}