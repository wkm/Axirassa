
package com.zanoccio.axirassa.domain;

import java.security.MessageDigest;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.classic.Session;

import com.zanoccio.axirassa.util.HibernateUtil;
import com.zanoccio.axirassa.util.MessageDigestProvider;
import com.zanoccio.axirassa.util.RandomStringGenerator;

public class User {

	/**
	 * @return true if the given username does not exist in the database
	 */
	public static boolean isEmailRegistered(String email) {
		System.out.println("Beginning query");
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Query query = session.createQuery("select user.Email from User as user where user.Email = ?");
		query.setString(0, email);

		System.out.println("Query: " + query.getQueryString());
		System.out.println("Retrieving list");
		List results = query.list();
		session.getTransaction().commit();

		if (results.size() > 0)
			return true;
		else
			return false;

	}


	//
	// Instance
	//

	private Long id;
	private String name;
	private String password;
	private String salt;

	private Date signupdate;
	private String email;
	private Boolean confirmed;
	private Boolean active;

	private Set accessEvents = new HashSet();


	public User() {
		confirmed = false;
		active = true;
		signupdate = new Date();
	}


	//
	// Logons
	//
	public Set getAccessEvents() {
		return accessEvents;
	}


	public void setAccessEvents(Set logons) {
		accessEvents = logons;
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
	// Salt
	//
	public void setSalt(String salt) {
		this.salt = salt;
	}


	public String getSalt() {
		return salt;
	}


	/**
	 * @return a 16-byte random string
	 */
	private String createSalt() {
		return RandomStringGenerator.getInstance().randomString(16);
	}


	//
	// Password
	//
	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		salt = createSalt();

		MessageDigest msgdigest = MessageDigestProvider.generate();
		msgdigest.update(MessageDigestProvider.salt());
		msgdigest.update(salt.getBytes());
		msgdigest.update(password.getBytes());

		this.password = new String(msgdigest.digest());
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
		if (name == null)
			name = email;

		this.email = email;
	}


	//
	// Confirmed
	//
	public Boolean isConfirmed() {
		return confirmed;
	}


	public void setConfirmed(Boolean confirmed) {
		this.confirmed = confirmed;
	}


	//
	// Active
	//
	public Boolean isActive() {
		return active;
	}


	public void setActive(Boolean active) {
		this.active = active;
	}


	//
	// ID
	//
	public void setID(Long id) {
		this.id = id;
	}


	public Long getID() {
		return id;
	}

}
