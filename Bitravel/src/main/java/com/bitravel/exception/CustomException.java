package com.bitravel.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1330263549017261897L;
	private final ErrorCode errorCode;

}