package com.metalancer.backend.common.exception;

import com.metalancer.backend.common.constants.ErrorCode;
import com.metalancer.backend.common.response.ErrorResponse;
import com.metalancer.backend.external.slack.SlackService;
import com.siot.IamportRestClient.exception.IamportResponseException;
import java.util.Arrays;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final SlackService slackService;
    private final Environment env;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> invalidRequestHandler(MethodArgumentNotValidException e) {
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

    @ExceptionHandler(IamportResponseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(IamportResponseException ex) {
        ErrorCode code = ErrorCode.PORTONE_ERROR;
        log.info(ex.getHttpStatusCode() + "-" + code.getMessage() + ": " + ex.getMessage());
        log.error(code + ": ", ex);

        switch (ex.getHttpStatusCode()) {
            case 401:
                //TODO
                break;
            case 500:
                //TODO
                break;
        }
        // 유저가 어떻게 에러가 났는지를 알 수 없게 code 로 보내주면 좋지만... 발생하는 에러 종류가 매번 다르기에
        ErrorResponse response = ErrorResponse.builder()
            .code(code.getCode())
            .message(code.getMessage() + "(" + ex.getMessage() + ")")
            .validation(new HashMap<>())
            .build();

        return new ResponseEntity<>(response,
            HttpStatus.valueOf(code.getStatus().value));
    }

    @ExceptionHandler(BaseException.class)
    protected ResponseEntity<ErrorResponse> handleBaseException(BaseException ex,
        WebRequest request) {
        String message = getUrlExceptionBrokeOut((ServletWebRequest) request);
        log.error(ex.getErrorCode() + ": ", ex);
        log.info(ex.getMessage());
        ErrorCode code = ex.getErrorCode();
        ErrorResponse response = ErrorResponse.builder()
            .code(code.getCode())
            .message(message + ": " + code.getMessage())
            .validation(new HashMap<>())
            .build();
        sendErrorSlackMessageIfProfileEqualsDev(response);
        response.setMessage(code.getMessage());
        return new ResponseEntity<>(response,
            HttpStatus.valueOf(code.getStatus().value));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, WebRequest request) {
        String message = getUrlExceptionBrokeOut((ServletWebRequest) request);

        ErrorResponse response = ErrorResponse.builder()
            .code(ErrorCode.SYSTEM_ERROR.getCode())
            .message(message + ": " + " 메시지: [" + ex.getMessage() + "]" + ", 이유: [" + ex.getCause()
                + "]")
            .validation(new HashMap<>())
            .build();

        log.error(response.getMessage());
        log.error("handleException", ex);
        ex.printStackTrace();
        sendErrorSlackMessageIfProfileEqualsDev(response);
        response.setMessage(" 메시지: [" + ex.getMessage() + "]" + ", 이유: [" + ex.getCause()
            + "]");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void sendErrorSlackMessageIfProfileEqualsDev(ErrorResponse response) {
        String[] activeProfiles = env.getActiveProfiles();
        if (Arrays.asList(activeProfiles).contains("dev")) {
            slackService.postSlackMessageToExceptionChannel(response.getMessage());
        }
    }

    @NotNull
    private static String getUrlExceptionBrokeOut(ServletWebRequest request) {
        String httpMethod = String.valueOf(request.getHttpMethod());
        String path = request.getRequest().getRequestURI();
        String message = "(Exception occurred at " + httpMethod + "-" + path + ")";
        log.info(message);
        return message;
    }
}