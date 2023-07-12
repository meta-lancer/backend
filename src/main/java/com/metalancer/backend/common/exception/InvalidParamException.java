package com.metalancer.backend.common.exception;


import com.metalancer.backend.common.constants.ErrorCode;

public class InvalidParamException extends BaseException {

    public InvalidParamException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvalidParamException(String errorMsg, ErrorCode errorCode) {
        super(errorMsg, errorCode);
    }

}