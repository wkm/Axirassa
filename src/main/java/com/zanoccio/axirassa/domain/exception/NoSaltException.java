
package com.zanoccio.axirassa.domain.exception;

import com.zanoccio.axirassa.domain.User;

public class NoSaltException extends Exception {
	private static final long serialVersionUID = 1L;


	public NoSaltException(User user) {
		super("Cannot hash password without a salt for: " + user);
	}

}
