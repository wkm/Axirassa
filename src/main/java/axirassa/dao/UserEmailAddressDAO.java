
package axirassa.dao;

import java.util.List;

import axirassa.model.UserEmailAddressEntity;
import axirassa.model.UserEntity;

public interface UserEmailAddressDAO {

	/**
	 * @return the {@link UserEmailAddressEntity} and an eagerly-fetched
	 *         <tt>user</tt> property, or null if no e-mail with the given id
	 *         exists.
	 */
	public UserEmailAddressEntity getByIdWithUser(long id);


	/**
	 * @return all email addresses associated with a user
	 */
	public List<UserEmailAddressEntity> getEmailsByUser(UserEntity user);


	/**
	 * @return the email address associated with the given token, or null if no
	 *         such e-mail exists.
	 */
	public UserEmailAddressEntity getByToken(String token);


	/**
	 * @return the email address entity for the given email address, or null
	 */
	public UserEmailAddressEntity getByEmail(String email);


	/**
	 * @return true if the e-mail is taken.
	 */
	public boolean isEmailRegistered(String email);


	/**
	 * @return the user associated with this e-mail.
	 */
	public UserEntity getUserByEmail(String email);


	/**
	 * @return the primary e-mail address entity associated with this user
	 *         (their authentication e-mail)
	 */
	public UserEmailAddressEntity getPrimaryEmail(UserEntity user);

}
