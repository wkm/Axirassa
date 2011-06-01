
package axirassa.webapp.services;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import axirassa.dao.UserEmailAddressDAO;
import axirassa.model.UserEntity;

/**
 * Based on suggestion from
 * http://permalink.gmane.org/gmane.comp.java.tynamo.user/155
 * 
 * @author wiktor
 */
public class EntityRealm extends AuthorizingRealm {
	public static final String REALM_NAME = "axirassarealm";

	private final UserEmailAddressDAO emailDAO;


	public EntityRealm(UserEmailAddressDAO emailDAO) {
		super(new MemoryConstrainedCacheManager());

		this.emailDAO = emailDAO;
		setName(REALM_NAME);
		setAuthenticationTokenClass(UsernamePasswordToken.class);
	}


	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		if (principals.isEmpty())
			return null;

		// make sure we have some authorization info from this realm
		if (principals.fromRealm(REALM_NAME).isEmpty())
			return null;

		String email = (String) principals.fromRealm(REALM_NAME).iterator().next();

		// no e-mail, no credentials
		if (email == null)
			return null;

		UserEntity user = emailDAO.getUserByEmail(email);
		if (user == null)
			return null;

		return new SimpleAuthorizationInfo(user.roles());
	}


	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken uptoken = (UsernamePasswordToken) token;

		String email = uptoken.getUsername();

		if (email == null)
			throw new AccountException("empty username for realm: " + REALM_NAME);

		// verify account exists
		UserEntity user = emailDAO.getUserByEmail(email);

		// retrieve the password and salt
		byte[] password = user.getPassword();
		byte[] salt = user.getSalt();

		return new UserAuthenticationInfo(email, password, salt);
	}

}
