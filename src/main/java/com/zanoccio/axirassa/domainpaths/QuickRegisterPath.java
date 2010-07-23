
package com.zanoccio.axirassa.domainpaths;

import org.hibernate.Session;

import com.zanoccio.axirassa.domain.AccountModel;
import com.zanoccio.axirassa.domain.AccountUserModel;
import com.zanoccio.axirassa.domain.UserModel;
import com.zanoccio.axirassa.domain.UserRole;
import com.zanoccio.axirassa.domain.exception.NoSaltException;

public class QuickRegisterPath extends AbstractDomainPath {

	private final String email;
	private final String password;


	public QuickRegisterPath(String email, String password) {
		this.email = email;
		this.password = password;
	}


	@Override
	public void execute(Session session) throws NoSaltException {
		UserModel user = new UserModel();
		AccountModel account = new AccountModel();
		AccountUserModel accountuser = new AccountUserModel();

		user.setEMail(email);
		user.createPassword(password);

		account.setName(email);

		accountuser.setRole(UserRole.ADMINISTRATOR);

		accountuser.setAccount(account);
		accountuser.setUser(user);

		session.save(user);
		session.save(account);
		session.save(accountuser);
	}
}
