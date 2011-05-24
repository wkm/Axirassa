
package axirassa.model.flows;

import org.apache.tapestry5.hibernate.annotations.CommitAfter;

import axirassa.model.UserEntity;

public interface ResetPasswordFlow extends Flow {

	public void setUserEntity(UserEntity user);


	@CommitAfter
	public void execute() throws Exception;
}
