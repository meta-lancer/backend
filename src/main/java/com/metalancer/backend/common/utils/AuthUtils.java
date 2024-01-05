package com.metalancer.backend.common.utils;

import com.metalancer.backend.common.config.security.PrincipalDetails;
import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.exception.BaseException;

public class AuthUtils {

    public static void validateUserAuthentication(PrincipalDetails user) {
        if (user == null) {
            throw new BaseException(ErrorCode.LOGIN_REQUIRED);
        }
    }

}
