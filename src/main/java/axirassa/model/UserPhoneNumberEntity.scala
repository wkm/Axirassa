package axirassa.model

import scala.reflect.BooleanBeanProperty
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
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import javax.persistence.ManyToOne
import javax.persistence.FetchType
import javax.persistence.Enumerated
import javax.persistence.EnumType
import axirassa.util.AutoSerializingObject
import axirassa.model.interceptor.EntityPreSave
import axirassa.util.RandomStringGenerator

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class UserPhoneNumberEntity extends AutoSerializingObject with Serializable with EntityPreSave with EntityWithUser {

	@Id
	@BeanProperty
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	var id: Long = _

	@BeanProperty
	@ManyToOne(optional = false)
	var user: UserEntity = _

	@BeanProperty
	@Basic(optional = false)
	var phoneNumber: String = _

	def getFormattedPhoneNumber() = {
		val formatted = new StringBuilder()

		formatted.append('(')
		formatted.append(phoneNumber.substring(0, 3))
		formatted.append(") ")
		formatted.append(phoneNumber.substring(3, 6))
		formatted.append("-")
		formatted.append(phoneNumber.substring(6))

		if (extension != null)
			formatted.append(" x").append(extension)

		formatted.toString()
	}

	@BeanProperty
	@Basic(optional = true)
	var extension: String = _

	@BooleanBeanProperty
	@Basic(optional = false)
	var confirmed: Boolean = _

	@BooleanBeanProperty
	@Basic(optional = false)
	var acceptingVoice: Boolean = _

	@BooleanBeanProperty
	@Basic(optional = false)
	var acceptingSms: Boolean = _

	@BeanProperty
	@Basic(optional = false)
	var token: String = _

	def getFormattedToken() = {
		val sb = new StringBuilder()

		val spanLength = 2
		for (i <- 0 until token.length by spanLength) {
			if (i != 0)
				sb.append("-")

			sb.append(token.substring(i, i + spanLength))
		}

		sb.toString()
	}

	def createToken() = {
		val tokenStr = RandomStringGenerator.makeRandomStringToken(8)
		tokenStr.toUpperCase()
	}

	override
	def preSave() {
		if (token == null)
			token = createToken()
	}
}