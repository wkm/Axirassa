
package com.zanoccio.axirassa.webapp;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;

public class LogonAction extends ActionSupport {

	private static final long serialVersionUID = 6411073863727392470L;


	@Override
	public String execute() {
		if (isInvalid(getUsername()))
			return INPUT;
		if (isInvalid(getPassword()))
			return INPUT;

		return SUCCESS;
	}


	private boolean isInvalid(String value) {
		if (value == null || value.length() < 1)
			return true;
		else
			return false;
	}


	private String _username;
	private String _password;


	public String getUsername() {
		return _username;
	}


	@RequiredFieldValidator(message = "you must provide a username")
	public void setUsername(String username) {
		_username = username;
	}


	public String getPassword() {
		return _password;
	}


	@RequiredFieldValidator(message = "you must provide a password")
	public void setPassword(String password) {
		_password = password;
	}

}
