package axirassa.webapp.services


import axirassa.dao.UserDAO
import axirassa.model.UserEntity 
import org.apache.shiro.authz.AuthorizationInfo
import org.apache.shiro.authz.SimpleAuthorizationInfo
import org.apache.shiro.cache.MemoryConstrainedCacheManager
import org.apache.shiro.realm.AuthorizingRealm
import org.apache.shiro.subject.PrincipalCollection


/**
 * Based on suggestion from http://permalink.gmane.org/gmane.comp.java.tynamo.user/155
 *
 * @author wiktor
 */
class EntityRealm extends AuthorizingRealm(new MemoryConstrainedCacheManager()) {
	val REALM_NAME = "axirassarealm"

	var userDAO : UserDAO = _

	 def this (userDAO : UserDAO ) {
		this.userDAO = userDAO
		setName(REALM_NAME)
		setAuthenticationTokenClass classOf[UsernamePasswordToken]
	}


	override
	protected def doGetAuthorizationInfo (principals : PrincipalCollection ) {
		if (principals.isEmpty())
			return null

		// make sure we have some authorization info from this realm
		if (principals.fromRealm(REALM_NAME).size() <= 0)
			return null

		val email = principals.fromRealm(REALM_NAME).iterator().next().asInstanceOf[String]

		// no e-mail, no credentials
		if (email == null)
			return null

		val user = userDAO.getUserByEmail(email)
		if (user.isEmpty)
			return null

		return new SimpleAuthorizationInfo(user.get.roles())
	}


	override
	protected def doGetAuthenticationInfo ( token : AuthenticationToken) {
		UsernamePasswordToken uptoken = ( UsernamePasswordToken ) token

		String email = uptoken.getUsername()

		if (email == null)
			throw new AccountException("empty username for realm: " + REALM_NAME)

		// verify account exists
		UserEntity user = userDAO.getUserByEmail(email)

		// retrieve the password and salt
		byte[] password = user.getPassword()
		String salt = user.getSalt()

		return new UserAuthenticationInfo(email, password, salt)
	}

}
