package com.metalancer.backend.common.exception;

import com.metalancer.backend.common.constants.ErrorCode;

public class EmptyParamException extends BaseException {

    public EmptyParamException(ErrorCode errorCode) {
        super(errorCode);
    }

    public EmptyParamException(String errorMsg, ErrorCode errorCode) {
        super(errorMsg, errorCode);
    }


}
