package com.inje.authservice.support.exception;

import com.inje.authservice.support.enumeration.ResponseTypeCodeEnumInterface;

import lombok.Getter;

/**
 * Custom Exception 사용 시 Base가 되는 Exception
 *
 * @author HakHyeon Song
 */
@Getter
public class BaseRuntimeException extends RuntimeException {

    private final ResponseTypeCodeEnumInterface exceptionCode;

    public BaseRuntimeException(ResponseTypeCodeEnumInterface exceptionCode, String loggingMessage) {
        super(loggingMessage);
        this.exceptionCode = exceptionCode;
    }

    public BaseRuntimeException(ResponseTypeCodeEnumInterface exceptionCode, Throwable e) {
        this.exceptionCode = exceptionCode;
        this.initCause(e);
    }
}
