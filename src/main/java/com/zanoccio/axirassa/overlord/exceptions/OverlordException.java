
package com.zanoccio.axirassa.overlord.exceptions;

abstract public class OverlordException extends Exception {
	private static final long serialVersionUID = -5086411247240858522L;


	public OverlordException(String msg) {
		super(msg);
	}


	public OverlordException(String msg, Exception cause) {
		super(msg, cause);
	}

}
