package com.metalancer.backend.common.constants;

import lombok.Getter;

@Getter
public class ValidationText {

    public final static String EMAIL_INVALID_REGEX = "invalid email regex";
    public final static String STRING_SHORT_LENGTH_VALIDATION = "size needs to be under 30";
    public final static String PASSWORD_LENGTH_VALIDATION = "password length is between 8 and 30";
    public final static String ID_OVER_ZERO_VALIDATION = "id should be over 0";
    public final static String TOKEN_EMPTY = "token empty";
    public final static String BOOLEAN_VALIDATION = "boolean type required";
    public final static String TRUE_REQUIRED = "true required";
    public final static String INVALID_PHONE = "invalid phone";
    public final static String SHOULD_BE_MORE_THAN_3 = "interests should be more than 3";
}
