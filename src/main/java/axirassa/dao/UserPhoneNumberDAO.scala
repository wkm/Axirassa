package axirassa.dao

import org.hibernate.Session
import org.apache.tapestry5.ioc.annotations.Inject
import axirassa.model.UserPhoneNumberEntity
import axirassa.model.UserEntity

trait UserPhoneNumberDAO {
    def getPhoneNumbersByUser(user : UserEntity) : List[UserPhoneNumberEntity]
    def getByIdWithUser(id : Long) : UserPhoneNumberEntity
}

class UserPhoneNumberDAOImpl extends UserPhoneNumberDAO {
    @Inject
    var database : Session = _

    override def getPhoneNumbersByUser(user : UserEntity) {
        val query = database.getNamedQuery("user_phonenumbers")
        query.setEntity("user", user)

        query.list.asInstanceOf[List[UserPhoneNumberEntity]]
    }

    override def getByIdWithUser(id : Long) = {
        val query = database.getNamedQuery("phonenumber_by_id")
        query.setLong("id", id)

        val list = query.list
        if (list.size < 1)
            null
        else
            list.get(0).asInstanceOf[UserPhoneNumberEntity]
    }
}