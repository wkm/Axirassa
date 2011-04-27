package axirassa.dao

import org.hibernate.Session
import org.apache.tapestry5.ioc.annotations.Inject
import axirassa.model.UserEntity

trait UserDAO {
    def isEmailRegistered(email : String) : Boolean
    def getUserByEmail(email : String) : UserEntity
}

class UserDAOImpl extends UserDAO {
    @Inject
    var database : Session = _

    override def isEmailRegistered(email : String) = {
        val query = database.getNamedQuery("user_is_email_registerd")
        query.setString("email", email)

        val results = query.list
        if (query.list.size > 0)
            true
        else
            false
    }

    override def getUserByEmail(email : String) = {
        val query = database.getNamedQuery("user_by_email")
        query.setString("email", email)

        val users = query.list
        if (users.size < 0)
            null
        else
            users.get(0).asInstanceOf[UserEntity]
    }
}