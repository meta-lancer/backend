package com.metalancer.backend.common.exception;

import com.metalancer.backend.common.constants.ErrorCode;

public class RoleException extends BaseException {

    public RoleException(ErrorCode errorCode) {
        super(errorCode);
    }

    public RoleException(String errorMsg, ErrorCode errorCode) {
        super(errorMsg, errorCode);
    }

}