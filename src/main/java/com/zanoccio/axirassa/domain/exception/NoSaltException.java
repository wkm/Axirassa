
package com.zanoccio.axirassa.domain.exception;

import com.zanoccio.axirassa.domain.UserModel;

public class NoSaltException extends Exception {
	private static final long serialVersionUID = 1L;


	public NoSaltException(UserModel userModel) {
		super("Cannot hash password without a salt for: " + userModel);
	}

}
