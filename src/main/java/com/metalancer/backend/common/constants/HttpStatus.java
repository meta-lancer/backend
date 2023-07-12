package com.metalancer.backend.common.constants;

import lombok.Getter;

@Getter
public enum HttpStatus {
    SUCCESS(200),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    NOT_FOUND_VALUE(404),
    DUPLICATED_VALUE(409),
    INVALID_ACCESS(403),
    INVALID_VALUE(422),
    UNEXPECTED_ERROR(500);

    public int value;

    HttpStatus(int value) {
        this.value = value;
    }

}