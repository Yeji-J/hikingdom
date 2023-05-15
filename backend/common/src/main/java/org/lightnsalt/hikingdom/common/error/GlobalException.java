package org.lightnsalt.hikingdom.common.error;

import lombok.Getter;

/**
 * Custom exception class
 */
@Getter
public class GlobalException extends RuntimeException {
	private final ErrorCode errorCode;

	public GlobalException(final ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
