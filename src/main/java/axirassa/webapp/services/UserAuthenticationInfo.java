
package axirassa.webapp.services;

import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.SimpleByteSource;

import axirassa.model.UserEntity;

public class UserAuthenticationInfo implements SaltedAuthenticationInfo {
	private static final long serialVersionUID = -239215165106391271L;


	public static UserAuthenticationInfo createInfoFromEntity(UserEntity user) {
		return new UserAuthenticationInfo(user.getPrimaryEmail(), user.getPassword(), user.getSalt());
	}


	//
	// INSTANCE VARIABLES AND METHODS
	//

	private final String email;
	private final byte[] password;
	private final String salt;


	public UserAuthenticationInfo(String email, byte[] password, String salt) {
		System.out.println("CREATING USER AUTH INFO FOR: " + email + " w/ salt: " + salt);

		this.email = email;
		this.password = password;
		this.salt = salt;
	}


	@Override
	public PrincipalCollection getPrincipals() {
		return new SimplePrincipalCollection(email, EntityRealm.REALM_NAME);
	}


	public String getEmail() {
		return email;
	}


	@Override
	public byte[] getCredentials() {
		return password;
	}


	@Override
	public ByteSource getCredentialsSalt() {
		return new SimpleByteSource(salt);
	}

}
