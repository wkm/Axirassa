package axirassa.model

import java.io.Serializable
import java.security.MessageDigest
import java.util.Collections
import java.util.Date
import java.util.Set

import javax.persistence.Basic
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Temporal
import javax.persistence.TemporalType

import scala.reflect.BeanProperty

import lombok.Getter
import lombok.Setter

import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

import axirassa.model.interceptor.EntityPreSave
import axirassa.util.AutoSerializingObject
import axirassa.util.MessageDigestProvider
import axirassa.util.RandomStringGenerator

object UserEntity {
  def hashPasswordWithSalt(password : String, salt : Array[Byte]) = {
    val msgdigest = MessageDigestProvider.generate()

    for (i <- 0 until 4096) {
      msgdigest.update(MessageDigestProvider.salt)
      msgdigest.update(salt)
      msgdigest.update(password.getBytes())
    }

    msgdigest.digest()
  }
}

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class UserEntity extends AutoSerializingObject with Serializable with EntityPreSave {

  @Id
  @BeanProperty
  @Basic(optional = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  protected var id : Long = _

  @BeanProperty
  @Basic(optional = false)
  protected var salt : String = _

  private def createSalt() = {
    RandomStringGenerator.instance.randomString(32)
  }

  @BeanProperty
  @Basic(optional = false)
  protected var password : Array[Byte] = _

  def createPassword(password : String) {
    if (salt == null)
      salt = createSalt();

    this.password = hashPassword(password)
  }

  def hashPassword(password : String) = {
    if (salt == null)
      throw new NoSaltException(this)

    UserEntity.hashPasswordWithSalt(password, salt.getBytes())
  }

  def matchPassword(password : String) : Boolean = {
    val hashed = hashPassword(password)

    if (hashed.length != this.password.length)
      return false

    for (i <- 0 until hashed.length)
      if (hashed(i) != this.password(i))
        return false

    return true
  }

  @BeanProperty
  @Temporal(TemporalType.TIMESTAMP)
  protected var signUpDate : Date = _

  def roles() = {
    Collections.singleton("user")
  }

  override def preSave() {
    if (signUpDate == null)
      signUpDate = new Date()
  }
}

class NoSaltException(user : UserEntity)
  extends Exception("Cannot hash password without a salt for "+user)