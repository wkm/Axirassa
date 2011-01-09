
package axirassa.domain;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.NaturalId;

import axirassa.domain.exception.NoSaltException;
import axirassa.util.MessageDigestProvider;
import axirassa.util.RandomStringGenerator;

@Entity
@Table(name = "Users")
public class UserModel implements Serializable {

	private static final long serialVersionUID = 1375674968928774909L;


	//
	// Static
	//

	public static boolean isEmailRegistered(Session session, String email) {
		Query query = session.createQuery("select user.EMail from UserModel as user where user.EMail = :email");
		query.setString("email", email);

		List results = query.list();
		boolean isregistered = false;
		if (results.size() > 0)
			return true;

		return isregistered;
	}


	//
	// Instance
	//

	private final Boolean confirmed = false;

	private final Boolean active = true;

	// @Embedded
	// private Set accessevents;

	// ID
	private Long id;


	@Id
	@Basic
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	// NAME
	private String name;


	@Basic
	@Column(name = "name")
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	// SALT
	private String salt;


	@Basic
	@Column(name = "salt")
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
	private byte[] password;


	@Basic
	@Column(name = "password")
	public byte[] getPassword() {
		return password;
	}


	/**
	 * Sets the password for this UserModel by salting and encrypting it
	 */
	public void createPassword(String password) throws NoSaltException {
		if (salt == null)
			salt = createSalt();

		this.password = hashPassword(password);
	}


	/**
	 * directly set the password, but without altering the salt. Use
	 * {@link #createPassword(String)} to create a salt.
	 */
	public void setPassword(byte[] password) throws NoSaltException {
		this.password = password;
	}


	public byte[] hashPassword(String password) throws NoSaltException {
		if (salt == null)
			throw new NoSaltException(this);

		MessageDigest msgdigest = MessageDigestProvider.generate();
		msgdigest.update(MessageDigestProvider.salt());
		msgdigest.update(salt.getBytes());
		msgdigest.update(password.getBytes());

		return msgdigest.digest();
	}


	/**
	 * @return gives true if the given password matches the recorded password
	 *         for this user when salted and encrypted.
	 */
	public boolean matchPassword(String password) throws NoSaltException {
		byte[] hashed = hashPassword(password);

		if (hashed.length != this.password.length)
			return false;

		for (int i = 0; i < hashed.length; i++)
			if (hashed[i] != this.password[i])
				return false;

		return true;
	}


	// SIGN UP DATE
	private Date signupdate;


	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "signupdate")
	public Date getSignUpDate() {
		return signupdate;
	}


	public void setSignUpDate(Date signupdate) {
		this.signupdate = signupdate;
	}


	// EMAIL
	private String email;


	@NaturalId
	@Column(name = "email")
	public String getEMail() {
		return email;
	}


	public void setEMail(String email) {
		this.email = email;

		if (name == null)
			name = email;
	}
}
