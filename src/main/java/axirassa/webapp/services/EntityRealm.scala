package axirassa.webapp.services

import org.apache.shiro.authc.AccountException
import org.apache.shiro.authc.UsernamePasswordToken
import org.apache.shiro.authc.AuthenticationToken

import axirassa.dao.UserDAO
import axirassa.model.UserEntity 
import org.apache.shiro.authz.AuthorizationInfo
import org.apache.shiro.authz.SimpleAuthorizationInfo
import org.apache.shiro.cache.MemoryConstrainedCacheManager
import org.apache.shiro.realm.AuthorizingRealm
import org.apache.shiro.subject.PrincipalCollection


object EntityRealm {
    val REALM_NAME = "axirassarealm"
}

/**
 * Based on suggestion from http://permalink.gmane.org/gmane.comp.java.tynamo.user/155
 *
 * @author wiktor
 */
class EntityRealm(userDAO : UserDAO) extends AuthorizingRealm(new MemoryConstrainedCacheManager()) {	    
	setName(EntityRealm.REALM_NAME)
	setAuthenticationTokenClass(classOf[UsernamePasswordToken])


	override protected
	def doGetAuthorizationInfo (principals : PrincipalCollection ) {
		if (principals.isEmpty())
			return null

		// make sure we have some authorization info from this realm
		if (principals.fromRealm(EntityRealm.REALM_NAME).size() <= 0)
			return null

		val email = principals.fromRealm(EntityRealm.REALM_NAME).iterator().next().asInstanceOf[String]

		// no e-mail, no credentials
		if (email == null)
			return null

		val user = userDAO.getUserByEmail(email)
		if (user.isEmpty)
			return null

		return new SimpleAuthorizationInfo(user.get.roles())
	}


	protected override
	def doGetAuthenticationInfo ( token : AuthenticationToken) = {
		val uptoken = token.asInstanceOf[UsernamePasswordToken] 

		val email = uptoken.getUsername()

		if (email == null)
			throw new AccountException("empty username for realm: " + EntityRealm.REALM_NAME)

		// verify account exists
		val user = userDAO.getUserByEmail(email)

		// retrieve the password and salt
		val password = user.get.getPassword()
		val salt = user.get.getSalt()

	  new UserAuthenticationInfo(email, password, salt)
	}

}
