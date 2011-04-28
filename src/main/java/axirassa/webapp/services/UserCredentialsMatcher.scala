
package axirassa.webapp.services

import java.util.Arrays

import org.apache.shiro.authc.AuthenticationInfo
import org.apache.shiro.authc.AuthenticationToken
import org.apache.shiro.authc.UsernamePasswordToken
import org.apache.shiro.authc.credential.CredentialsMatcher
import org.hibernate.Session

import axirassa.model.UserEntity

class UserCredentialsMatcher(session : Session) extends CredentialsMatcher {
    /**
     * Validate the user's credentials (the {@link AuthenticationToken}) against
     * records ({@link AuthenticationInfo}).
     */
    override def doCredentialsMatch(token : AuthenticationToken, info : AuthenticationInfo) : Boolean = {
        if (!token.isInstanceOf[UsernamePasswordToken]) {
            System.err.println("Invalid authentication token object")
            return false
        }

        if (!info.isInstanceOf[UserAuthenticationInfo]) {
            System.err.println("Invalid authentication info object")
            return false
        }

        val uptoken = token.asInstanceOf[UsernamePasswordToken]
        val authinfo = info.asInstanceOf[UserAuthenticationInfo]

        // check that the e-mails match
        if (!uptoken.getUsername().equals(authinfo.getEmail())) {
            System.err.println("Username mismatch between authenticaion token and information")
            return false
        }

        val hashedpass = UserEntity.hashPasswordWithSalt(new String(uptoken.getPassword()), authinfo
            .getCredentialsSalt().getBytes())

        return Arrays.equals(hashedpass, authinfo.getCredentials())
    }
}
