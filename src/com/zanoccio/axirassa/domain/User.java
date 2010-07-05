
package com.zanoccio.axirassa.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class User {

	private String name;
	private String username;
	private String password;

	private Date signupdate;
	private String email;
	private boolean confirmed;
	private boolean active;

	private Set accessEvents = new HashSet();


	public User() {
	}


	//
	// Logons
	//
	public Set getAccessEvents() {
		return accessEvents;
	}


	public void setAccessEvents(Set logons) {
		this.accessEvents = logons;
	}


	//
	// Name
	//
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	//
	// Password
	//
	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	//
	// Username
	//
	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	//
	// SignUpDate
	//
	public Date getSignUpDate() {
		return signupdate;
	}


	public void setSignUpDate(Date signupdate) {
		this.signupdate = signupdate;
	}


	//
	// Email
	//
	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	//
	// Confirmed
	//
	public boolean isConfirmed() {
		return confirmed;
	}


	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}


	//
	// Active
	//
	public boolean isActive() {
		return active;
	}


	public void setActive(boolean active) {
		this.active = active;
	}

}
