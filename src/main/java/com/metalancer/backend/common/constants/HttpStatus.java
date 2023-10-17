package com.metalancer.backend.common.constants;

import lombok.Getter;

@Getter
public enum HttpStatus {
    SUCCESS(200),
    CREATED(201),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    INVALID_ACCESS(403),
    NOT_FOUND_VALUE(404),
    DUPLICATED_VALUE(409),
    INVALID_VALUE(422),
    UNEXPECTED_ERROR(500),
    EXTERNAL_API_CALL_FAILED(502);

    public int value;

    HttpStatus(int value) {
        this.value = value;
    }

}