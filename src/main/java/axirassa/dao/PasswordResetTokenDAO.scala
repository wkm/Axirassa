package axirassa.dao

import java.util.Date
import org.hibernate.Session
import org.apache.tapestry5.ioc.annotations.Inject
import axirassa.model.PasswordResetTokenEntity

trait PasswordResetTokenDAO {
  def getByToken(token : String) : Option[PasswordResetTokenEntity]
  def removeExpiredTokens : Int
}

class PasswordResetTokenDAOImpl extends PasswordResetTokenDAO {
  @Inject
  var database : Session = _

  override def getByToken(token : String) = {
    val query = database.getNamedQuery("password_reset_token")
    query.setString("token", token)
    query.setTimestamp("date", new Date)

    val results = query.list
    if (results.size < 1)
      None
    else
      Some(results.get(0).asInstanceOf[PasswordResetTokenEntity])
  }

  override def removeExpiredTokens = {
    val query = database.getNamedQuery("remove_expired_tokens")
    query.setTimestamp("date", new Date)

    query.executeUpdate
  }
}