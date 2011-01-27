
package axirassa.webapp.services;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.MemoryConstrainedCacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.hibernate.Session;

import axirassa.domain.UserModel;
import axirassa.domain.exception.NoSaltException;

/**
 * Based on suggestion from
 * http://permalink.gmane.org/gmane.comp.java.tynamo.user/155
 * 
 * @author wiktor
 */
public class EntityRealm extends AuthorizingRealm {
	public static final String REALM_NAME = "axirassarealm";

	private final Session session;


	public EntityRealm(Session session) {
		super(new MemoryConstrainedCacheManager());

		this.session = session;
		setName(REALM_NAME);
		setAuthenticationTokenClass(UsernamePasswordToken.class);
	}


	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		System.out.println("GETTING AUTHORIZATION INFO");

		if (principals.isEmpty())
			return null;

		// make sure we have some authorization info from this realm
		if (principals.fromRealm(REALM_NAME).size() <= 0)
			return null;

		String email = (String) principals.fromRealm(REALM_NAME).iterator().next();

		// no e-mail, no credentials
		if (email == null)
			return null;

		UserModel user = UserModel.getUserByEmail(session, email);
		if (user == null)
			return null;

		return new SimpleAuthorizationInfo(user.roles());
	}


	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		System.out.println("GETTING AUTHENTICATION INFO");

		UsernamePasswordToken uptoken = (UsernamePasswordToken) token;
		System.out.println("USER: " + uptoken.getUsername());
		System.out.println("PASSWORD: " + new String(uptoken.getPassword()));
		System.out.println("HOST: " + uptoken.getHost());

		String email = uptoken.getUsername();

		if (email == null)
			throw new AccountException("empty username for realm: " + REALM_NAME);

		// verify account exists
		UserModel user = UserModel.getUserByEmail(session, email);
		try {
			if (user.matchPassword(new String(uptoken.getPassword()))) {
				System.out.println("PASSWORD MATCH");
			} else {
				System.out.println("PASSWORD FAIL");
				return null;
			}
		} catch (NoSaltException e) {
			throw new AccountException("No salt stored for account");
		}

		return new SimpleAuthenticationInfo(email, user.getPassword(), REALM_NAME);
	}

}
