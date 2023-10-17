package com.metalancer.backend.common.exception;

import com.metalancer.backend.common.constants.ErrorCode;

public class DataStatusException extends BaseException {

    public DataStatusException(ErrorCode errorCode) {
        super(errorCode);
    }

    public DataStatusException(String errorMsg, ErrorCode errorCode) {
        super(errorMsg, errorCode);
    }

}