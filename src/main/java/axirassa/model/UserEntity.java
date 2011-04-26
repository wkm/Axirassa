
package axirassa.model;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import axirassa.model.exception.NoSaltException;
import axirassa.model.interceptor.EntityPreSave;
import axirassa.util.AutoSerializingObject;
import axirassa.util.MessageDigestProvider;
import axirassa.util.RandomStringGenerator;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserEntity extends AutoSerializingObject implements Serializable, EntityPreSave {
	private static final long serialVersionUID = 1375674968928774909L;


	public static byte[] hashPasswordWithSalt (String password, byte[] salt) {
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
	@Setter
	private Long id;


	public Long getId () {
    	return id;
    }


	// SALT
	@Getter
	@Setter
	@Basic(optional = false)
	private String salt;


	private String createSalt () {
		// 32 * 8 = 256 bits
		return RandomStringGenerator.getInstance().randomString(32);
	}


	// PASSWORD
	@Getter
	@Setter
	@Basic(optional = false)
	private byte[] password;

	/**
	 * Sets the password for this UserEntity by salting and encrypting it
	 */
	public void createPassword (String password) {
		if (salt == null)
			salt = createSalt();

		try {
			setPassword(hashPassword(password));
		} catch (NoSaltException e) {
			e.printStackTrace();
		}
	}


	public byte[] hashPassword (String password) throws NoSaltException {
		if (salt == null)
			throw new NoSaltException(this);

		return hashPasswordWithSalt(password, salt.getBytes());
	}


	/**
	 * @return gives true if the given password matches the recorded password
	 *         for this user when salted and encrypted.
	 */
	public boolean matchPassword (String password) throws NoSaltException {
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
	@Getter
	@Setter
	@Temporal(TemporalType.TIMESTAMP)
	private Date signUpDate;


	// ROLES

	/**
	 * a placeholder function which just returns <"user"> until we have a need
	 * for actual roles
	 */
	public Set<String> roles () {
		return Collections.singleton("user");
	}


	@Override
	public void preSave () {
		if (signUpDate == null)
			signUpDate = new Date();
	}
}
