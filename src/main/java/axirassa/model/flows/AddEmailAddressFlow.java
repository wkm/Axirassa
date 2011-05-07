
package axirassa.model.flows;

import axirassa.model.UserEntity;

public interface AddEmailAddressFlow extends Flow {
	public void setUserEntity(UserEntity user);


	public void setEmailAddress(String email);
}
