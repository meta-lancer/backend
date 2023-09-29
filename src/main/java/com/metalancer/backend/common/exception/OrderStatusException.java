package com.metalancer.backend.common.exception;

import com.metalancer.backend.common.constants.ErrorCode;

public class OrderStatusException extends BaseException {

    public OrderStatusException(ErrorCode errorCode) {
        super(errorCode);
    }

    public OrderStatusException(String errorMsg, ErrorCode errorCode) {
        super(errorMsg, errorCode);
    }

}