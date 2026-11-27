package com.dongnerun.backend.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * 컨트롤러에서 발생한 예외를 한곳에서 받아 ApiError(JSON) 형식으로 바꿔 응답한다.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 잘못된 입력값 (예: 목표 거리 0km, 위도 91도)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException e) {
        return ApiError.of(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    // 필수 파라미터 누락 (예: targetKm 없음)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> handleMissingParameter(MissingServletRequestParameterException e) {
        return ApiError.of(HttpStatus.BAD_REQUEST, "필수 값 '%s'이(가) 없습니다.".formatted(e.getParameterName()));
    }

    // 파라미터 형식 오류 (예: targetKm=abc)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        return ApiError.of(HttpStatus.BAD_REQUEST, "'%s' 값의 형식이 올바르지 않습니다.".formatted(e.getName()));
    }

    // 그 밖의 모든 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception e) {
        // 없는 주소(404), 허용하지 않는 메서드(405)처럼 스프링이 상태 코드를 정해 둔 예외는 그 코드를 그대로 쓴다.
        if (e instanceof ErrorResponse springError) {
            HttpStatus status = HttpStatus.valueOf(springError.getStatusCode().value());
            return ApiError.of(status, status.getReasonPhrase());
        }
        // 예상하지 못한 오류는 원인을 로그로 남기고, 사용자에게는 내부 정보를 숨긴다.
        log.error("처리하지 못한 예외", e);
        return ApiError.of(HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 오류가 발생했습니다.");
    }
}