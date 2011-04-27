package axirassa.dao

import org.apache.tapestry5.ioc.annotations.Inject
import org.hibernate.Session
import axirassa.model.UserEntity
import axirassa.model.UserEmailAddressEntity

trait UserEmailAddressDAO {
    /**
     * @return the {@link UserEmailAddressEntity} and an eagerly-fetched
     *         <tt>user</tt> property, or null if no e-mail with the given id
     *         exists.
     */
    def getByIdWithUser(id : Long) : UserEmailAddressEntity

    /**
     * @return all email addresses associated with a user
     */
    def getEmailsByUser(user : UserEntity) : List[UserEmailAddressEntity]

    /**
     * @return the email address associated with the given token, or null if no
     *         such e-mail exists.
     */
    def getByToken(token : String) : UserEmailAddressEntity

    /**
     * @return true if the e-mail is taken.
     */
    def isEmailRegistered(email : String) : Boolean

    /**
     * @return the user associated with this e-mail.
     */
    def getUserByEmail(email : String) : UserEntity

    /**
     * @return the primary e-mail address entity associated with this user
     *         (their authentication e-mail)
     */
    def getPrimaryEmail(user : UserEntity) : UserEmailAddressEntity
}

class UserEmailAddressDAOImpl extends UserEmailAddressDAO {
    @Inject
    var database : Session = _

    override def isEmailRegistered(email : String) = {
        val query = database.getNamedQuery("get_email")
        query.setString("email", email)

        if (query.list.isEmpty)
            false
        else
            true
    }

    override def getUserByEmail(email : String) = {
        val query = database.getNamedQuery("get_user_by_email")
        query.setString("email", email)

        val emails = query.list
        if (emails.isEmpty)
            null
        else
            emails.get(0).asInstanceOf[UserEntity]
    }

    override def getByIdWithUser(id : Long) {
        val query = database.getNamedQuery("email_by_id")
        query.setLong("id", id)

        val emails = query.list
        if (emails.isEmpty)
            null
        else
            emails.get(0).asInstanceOf[UserEmailAddressEntity]
    }

    override def getEmailsByUser(user : UserEntity) {
        val query = database.getNamedQuery("user_emails")
        query.setEntity("user", user)
        query.list.asInstanceOf[List[UserEmailAddressEntity]]
    }

    override def getPrimaryEmail(user : UserEntity) {
        val query = database.getNamedQuery("user_primary_email")
        query.setEntity("user", user)

        val emails = query.list
        if (emails.isEmpty)
            null
        else
            emails.get(0).asInstanceOf[UserEmailAddressEntity]
    }

    override def getByToken(token : String) {
        val query = database.getNamedQuery("email_by_token")
        query.setString("token", token)

        val email = query.list
        if (email.isEmpty)
            null
        else
            email.get(0).asInstanceOf[UserEmailAddressEntity]
    }
}