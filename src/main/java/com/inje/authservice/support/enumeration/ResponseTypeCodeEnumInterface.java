package com.inje.authservice.support.enumeration;

import org.springframework.http.HttpStatus;

/**
 * CommonResponse 에서 공통 처리 하기 위한 interface.
 *
 * @author HakHyeon Song
 */
public interface ResponseTypeCodeEnumInterface {

    HttpStatus getHttpStatus();

    Integer getResultCode();

    String getMessage();
}
