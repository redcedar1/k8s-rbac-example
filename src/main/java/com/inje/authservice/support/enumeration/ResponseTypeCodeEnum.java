package com.inje.authservice.support.enumeration;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Success/Error Response Type Code Enum
 *
 * @author HakHyeon Song
 */
@Getter
@RequiredArgsConstructor
public enum ResponseTypeCodeEnum implements ResponseTypeCodeEnumInterface {
    // 200x
    SUCCESS(HttpStatus.OK, 2000, "system.message.success.ok"),
    REFRESH_TOKEN(HttpStatus.OK, 2001, "system.message.success.refreshToken"),
    PARTIAL_SUCCESS(HttpStatus.OK, 2002, "system.message.success.partialSuccess"),
    CHANGED_SUCCESS(HttpStatus.OK, 2003, "system.message.success.changedSuccess"),

    // 400x
    BAD_REQUEST(HttpStatus.OK, 4001, "system.message.exception.badRequest"),
    REQUEST_INVALID(HttpStatus.OK, 4002, "system.message.exception.invalidRequest"),

    // 401x
    UNAUTHORIZED(HttpStatus.OK, 4011, "system.message.exception.unauthorized"),
    TOKEN_EXPIRED(HttpStatus.OK, 4012, "system.message.exception.expiredAccessToken"),

    // 403x
    FORBIDDEN(HttpStatus.OK, 4031, "system.message.exception.forbidden"),

    // 404x
    NOT_FOUND(HttpStatus.OK, 4041, "system.message.exception.nonExistValue"),
    RESOURCE_NOT_FOUND(HttpStatus.OK, 4042, "system.message.exception.noResource"),

    // 409x
    // 중복
    CONFLICT(HttpStatus.OK, 4091, "system.message.exception.conflict"),
    EXIST_VALUE_CONFLICT(HttpStatus.OK, 4091, "system.message.exception.existValue"),
    // 상태 충돌
    UNMODIFIABLE_STATUS_CONFLICT(HttpStatus.OK, 4092, "system.message.exception.unmodifiableStatus"),
    // 개수 한계
    RESOURCE_LIMIT_CONFLICT(HttpStatus.OK, 4093, "system.message.exception.resourceLimitOver"),

    // 500x
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 5001, "system.message.error.internalServerError"),
    EXTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 5001, "system.message.error.externalServerError"),
    JSON_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 5001, "system.message.error.jsonProcessingError"),
    INTERNAL_TIME_OUT(HttpStatus.INTERNAL_SERVER_ERROR, 5002, "system.message.error.internalTimeOut"),
    KEYCLOAK_UNAVAILABLE(HttpStatus.OK, 5003, "Keycloak 서비스 사용 불가");

    private final HttpStatus httpStatus;
    private final Integer resultCode;
    private final String message;

    ResponseTypeCodeEnum(HttpStatus httpStatus, int resultCode, String message) {
        this.httpStatus = httpStatus;
        this.resultCode = resultCode;
        this.message = message;
    }

    @Override public HttpStatus getHttpStatus() { return httpStatus; }
    @Override public Integer getResultCode() { return resultCode; }
    @Override public String getMessage() { return message; }
}
