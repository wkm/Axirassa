
package axirassa.webapp.services;

import java.util.Arrays;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.hibernate.Session;

import axirassa.model.UserEntity;

public class UserCredentialsMatcher implements CredentialsMatcher {

	private final Session session;


	public UserCredentialsMatcher(Session session) {
		this.session = session;
	}


	/**
	 * Validate the user's credentials (the {@link AuthenticationToken}) against
	 * records ({@link AuthenticationInfo}).
	 */
	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		if (!(token instanceof UsernamePasswordToken)) {
			System.err.println("Invalid authentication token object");
			return false;
		}

		if (!(info instanceof UserAuthenticationInfo)) {
			System.err.println("Invalid authentication info object");
			return false;
		}

		UsernamePasswordToken uptoken = (UsernamePasswordToken) token;
		UserAuthenticationInfo authinfo = (UserAuthenticationInfo) info;

		// check that the e-mails match
		if (!uptoken.getUsername().equals(authinfo.getEmail())) {
			System.err.println("Username mismatch between authenticaion token and information");
			return false;
		}

		byte[] hashedpass = UserEntity.hashPasswordWithSalt(new String(uptoken.getPassword()), authinfo
		        .getCredentialsSalt().getBytes());

		System.err.println("UCM: PASSWORD: " + new String(uptoken.getPassword()));
		System.err.println("UCM: SALT: " + new String(authinfo.getCredentialsSalt().getBytes()));

		return Arrays.equals(hashedpass, authinfo.getCredentials());
	}
}
