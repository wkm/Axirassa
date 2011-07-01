
package axirassa.model.flows;

import org.apache.tapestry5.hibernate.annotations.CommitAfter;

import axirassa.model.UserEmailAddressEntity;
import axirassa.model.UserEntity;

/**
 * Create a user and notify their e-mail of the user's creation.
 * 
 * @author wiktor
 */
public interface CreateUserFlow extends Flow {

	public abstract void setPassword(String password);


	public abstract void setEmail(String email);


	public UserEntity getUserEntity();


	public UserEmailAddressEntity getPrimaryEmailEntity();


	@Override
	@CommitAfter
	public void execute() throws Exception;

}
