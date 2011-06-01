
package axirassa.model.flows;

import org.apache.tapestry5.hibernate.annotations.CommitAfter;

import axirassa.model.UserEntity;

public interface AddEmailAddressFlow extends Flow {
	public void setUserEntity(UserEntity user);


	public void setEmailAddress(String email);


	@CommitAfter
	public void execute() throws Exception;
}
