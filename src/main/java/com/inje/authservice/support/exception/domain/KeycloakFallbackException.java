package com.inje.authservice.support.exception.domain;

import com.inje.authservice.support.enumeration.ResponseTypeCodeEnum;
import com.inje.authservice.support.exception.BaseRuntimeException;

public class KeycloakFallbackException extends BaseRuntimeException {
    public KeycloakFallbackException(String detailMessage) {
        super(ResponseTypeCodeEnum.KEYCLOAK_UNAVAILABLE, detailMessage);
    }
}
