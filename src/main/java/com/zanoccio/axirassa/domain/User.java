
package com.zanoccio.axirassa.domain;

import java.security.MessageDigest;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.Query;
import org.hibernate.annotations.NaturalId;
import org.hibernate.classic.Session;

import com.zanoccio.axirassa.util.HibernateUtil;

@Entity
@Table(name = "Users")
public class User {

	//
	// Static
	//

	public static boolean isEmailRegistered(String email) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		session.beginTransaction();

		Query query = session.createQuery("select user.Email from User as user where user.Email = ?");
		query.setString(0, email);

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

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	private Long id;

	private String name;

	private String password;

	private String salt;

	private Date signupdate;

	@NaturalId
	private String email;

	private Boolean confirmed;

	private Boolean active;

	@Embedded
	private Set accessevents;


	// ID
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	// NAME
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	// SALT
	public String getSalt() {
		return salt;
	}


	public void setSalt(String salt) {
		this.salt = salt;
	}


	private String createSalt() {
		return RandomStringGenerator.getInstance().randomString(16);
	}


	// PASSWORD
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


	// SIGN UP DATE
	public Date getSignUpDate() {
		return signupdate;
	}


	public void setSignUpDate(Date signupdate) {
		this.signupdate = signupdate;
	}


	// EMAIL
	public String getEMail() {
		return email;
	}


	public void setEMail(String email) {
		this.email = email;

		if (name == null)
			name = email;
	}
}
