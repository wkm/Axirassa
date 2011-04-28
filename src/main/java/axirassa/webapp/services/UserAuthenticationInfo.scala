
package axirassa.webapp.services

import org.apache.shiro.authc.SaltedAuthenticationInfo
import org.apache.shiro.subject.PrincipalCollection
import org.apache.shiro.subject.SimplePrincipalCollection
import org.apache.shiro.util.ByteSource
import org.apache.shiro.util.SimpleByteSource

import axirassa.model.UserEmailAddressEntity

object UserAuthenticationInfo {
    def createInfoFromEntity(email : UserEmailAddressEntity) =
        new UserAuthenticationInfo(email.getEmail, email.getUser.getPassword, email.getUser.getSalt)
}

class UserAuthenticationInfo(email : String, password : Array[Byte], salt : String) extends SaltedAuthenticationInfo {
    override def getPrincipals = new SimplePrincipalCollection(email, EntityRealm.REALM_NAME)

    def getEmail() = email

    override def getCredentials = password

    override def getCredentialsSalt = new SimpleByteSource(salt)
}
