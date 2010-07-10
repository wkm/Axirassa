
package com.zanoccio.axirassa.webapp;

import com.opensymphony.xwork2.ActionSupport;

public class LogonAction extends ActionSupport {

	private static final long serialVersionUID = 6411073863727392470L;


	@Override
	public String execute() {
		if (isInvalid(getEmail()))
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


	private String _email;
	private String _password;


	public String getEmail() {
		return _email;
	}


	public void setEmail(String username) {
		_email = username;
	}


	public String getPassword() {
		return _password;
	}


	public void setPassword(String password) {
		_password = password;
	}

}
