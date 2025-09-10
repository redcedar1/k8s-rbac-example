package com.inje.authservice.support.exception.domain;

import com.inje.authservice.support.enumeration.ResponseTypeCodeEnum;
import com.inje.authservice.support.exception.BaseRuntimeException;

/**
 * 사용자 조회 시 해당 사용자가 존재하지 않는 경우 발생하는 Exception.
 *
 * @author HakHyeon Song
 */
public class UserNotFoundException extends BaseRuntimeException {

	public UserNotFoundException(String loggingMessage) {
		super(ResponseTypeCodeEnum.NOT_FOUND, loggingMessage);
	}

	public UserNotFoundException(Throwable e) {
		super(ResponseTypeCodeEnum.NOT_FOUND, e);
	}
}
