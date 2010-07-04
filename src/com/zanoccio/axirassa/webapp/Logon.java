package com.zanoccio.axirassa.webapp;

import com.opensymphony.xwork2.ActionSupport;

public class Logon extends ActionSupport {

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

	public void setUsername(String username) {
		_username = username;
	}

	public String getPassword() {
		return _password;
	}

	public void setPassword(String password) {
		_password = password;
	}

}
