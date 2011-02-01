
package axirassa.model.exception;

import axirassa.model.UserEntity;

public class NoSaltException extends Exception {
	private static final long serialVersionUID = 1L;


	public NoSaltException(UserEntity userModel) {
		super("Cannot hash password without a salt for: " + userModel);
	}

}
