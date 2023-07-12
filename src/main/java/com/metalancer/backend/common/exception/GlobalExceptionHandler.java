package com.metalancer.backend.common.exception;

import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.response.ErrorResponse;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity invalidRequestHandler(MethodArgumentNotValidException e) {
        ErrorCode invalidParameterCode = ErrorCode.INVALID_PARAMETER;
        ErrorResponse response = ErrorResponse.builder()
            .code(invalidParameterCode.getCode())
            .message(invalidParameterCode.getMessage())
            .validation(new HashMap<>())
            .build();

        for (FieldError fieldError : e.getFieldErrors()) {
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(response,
            HttpStatus.valueOf(HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(BaseException.class)
    protected ResponseEntity handleBaseException(BaseException ex) {
        log.error(ex.getErrorCode() + ": ", ex);
        log.info(ex.getMessage());
        ErrorCode code = ex.getErrorCode();
        ErrorResponse response = ErrorResponse.builder()
            .code(code.getCode())
            .message(code.getMessage())
            .validation(new HashMap<>())
            .build();

        return new ResponseEntity<>(response,
            HttpStatus.valueOf(code.getStatus().value));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {

        ErrorResponse response = ErrorResponse.builder()
            .code(ErrorCode.SYSTEM_ERROR.getCode())
            .message(" 메시지: [" + ex.getMessage() + "]" + ", 이유: [" + ex.getCause() + "]")
            .validation(new HashMap<>())
            .build();

        log.error(response.getMessage());
        log.error("handleException", ex);
        ex.printStackTrace();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}