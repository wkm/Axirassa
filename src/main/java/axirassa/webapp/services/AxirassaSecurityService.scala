package axirassa.webapp.services

import axirassa.dao.UserEmailAddressDAO
import axirassa.dao.UserDAO
import org.hibernate.Session
import org.tynamo.security.services.SecurityService
import org.apache.tapestry5.ioc.annotations.Inject
import axirassa.model.EntityWithUser
import org.apache.shiro.subject.Subject
import axirassa.model.UserEntity


class AxirassaSecurityException extends Exception("Unauthorized access attempt")


trait AxirassaSecurityService {
    def getEmail : String
    def getUserEntity : UserEntity
    def isUser : Boolean
    def isGuest : Boolean
    def isAuthenticated : Boolean
    def getSubject : Subject
    def verifyOwnership(entity : EntityWithUser)
}

class AxirassaSecurityServiceImpl extends AxirassaSecurityService {
    @Inject
    var security : SecurityService = _

    @Inject
    var database : Session = _

    @Inject
    var userDAO : UserDAO = _

    @Inject
    var userEmailAddressDAO : UserEmailAddressDAO = _

    override def isAuthenticated = security.isAuthenticated
    override def isGuest = security.isGuest
    override def isUser = security.isUser
    override def getEmail = security.getSubject.getPrincipal.asInstanceOf[String]
    override def getSubject = security.getSubject

    override def verifyOwnership(entity : EntityWithUser) {
        if (entity.getUser == null)
            throw new AxirassaSecurityException

        val user = entity.getUser
        val email = userEmailAddressDAO.getPrimaryEmail(user)

        // user has no primary e-mail (weird)
        if (email.isEmpty)
            throw new AxirassaSecurityException

        if (getEmail != email.get)
            throw new AxirassaSecurityException
    }

    override def getUserEntity = {
        if (getEmail == null)
            null
        else {
            val user = userDAO.getUserByEmail(getEmail)
            if (user.isEmpty)
                throw new AxirassaSecurityException

            user.get
        }
    }
}