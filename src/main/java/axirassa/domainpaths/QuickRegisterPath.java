
package axirassa.domainpaths;

import org.hibernate.Session;

import axirassa.domain.AccountModel;
import axirassa.domain.AccountUserModel;
import axirassa.domain.UserModel;
import axirassa.domain.UserRole;
import axirassa.domain.exception.NoSaltException;

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
