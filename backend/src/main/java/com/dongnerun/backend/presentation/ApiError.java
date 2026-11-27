package com.dongnerun.backend.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 모든 에러 응답의 공통 형식.
 * 예: {"status":400,"error":"Bad Request","message":"목표 거리는 1~50km 사이여야 합니다."}
 */
public record ApiError(int status, String error, String message) {

    public static ResponseEntity<ApiError> of(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(new ApiError(status.value(), status.getReasonPhrase(), message));
    }
}