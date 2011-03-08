
package axirassa.model;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.Query;
import org.hibernate.Session;

import axirassa.model.exception.NoSaltException;
import axirassa.model.interceptor.EntityPreSave;
import axirassa.util.AutoSerializingObject;
import axirassa.util.MessageDigestProvider;
import axirassa.util.RandomStringGenerator;

@Entity
public class UserEntity extends AutoSerializingObject implements Serializable, EntityPreSave {
	private static final long serialVersionUID = 1375674968928774909L;


	//
	// Static
	//

	public static boolean isEmailRegistered(Session session, String email) {
		Query query = session.getNamedQuery("user_is_email_registered");
		query.setString("email", email);

		List results = query.list();
		boolean isregistered = false;
		if (results.size() > 0)
			return true;

		return isregistered;
	}


	public static UserEntity getUserByEmail(Session session, String email) {
		Query query = session.getNamedQuery("user_by_email");
		query.setString("email", email);

		List<UserEntity> users = query.list();

		if (users.size() <= 0)
			return null;

		return users.iterator().next();
	}


	public static byte[] hashPasswordWithSalt(String password, byte[] salt) {
		MessageDigest msgdigest = MessageDigestProvider.generate();

		for (int i = 0; i < 4096; i++) {
			msgdigest.update(MessageDigestProvider.salt());
			msgdigest.update(salt);
			msgdigest.update(password.getBytes());
		}

		return msgdigest.digest();
	}


	//
	// Instance
	//

	private final Boolean confirmed = false;

	private final Boolean active = true;

	// @Embedded
	// private Set accessevents;

	// ID
	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	// SALT
	@Basic(optional = false)
	private String salt;


	public String getSalt() {
		return salt;
	}


	public void setSalt(String salt) {
		this.salt = salt;
	}


	private String createSalt() {
		// 32 * 8 = 256 bits
		return RandomStringGenerator.getInstance().randomString(32);
	}


	// PASSWORD
	@Basic(optional = false)
	private byte[] password;


	public byte[] getPassword() {
		return password;
	}


	/**
	 * Sets the password for this UserEntity by salting and encrypting it
	 */
	public void createPassword(String password) {
		if (salt == null)
			salt = createSalt();

		try {
			setPassword(hashPassword(password));
		} catch (NoSaltException e) {
			e.printStackTrace();
		}
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

		return hashPasswordWithSalt(password, salt.getBytes());
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
	@Basic
	@Temporal(TemporalType.TIMESTAMP)
	private Date signupdate;


	public Date getSignUpDate() {
		return signupdate;
	}


	public void setSignUpDate(Date signupdate) {
		this.signupdate = signupdate;
	}


	// ROLES

	/**
	 * a placeholder function which just returns <"user"> until we have a need
	 * for actual roles
	 */
	public Set<String> roles() {
		return Collections.singleton("user");
	}


	@Override
	public void preSave() {
		if (signupdate == null)
			signupdate = new Date();
	}
}
