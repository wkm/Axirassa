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
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import javax.persistence.ManyToOne
import javax.persistence.FetchType
import javax.persistence.Enumerated
import javax.persistence.EnumType
import axirassa.util.AutoSerializingObject
import axirassa.model.interceptor.EntityPreSave
import axirassa.util.RandomStringGenerator
import org.hibernate.annotations.NaturalId
import java.util.Calendar

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class PasswordResetTokenEntity extends Serializable with EntityPreSave {
	@Id
	@BeanProperty
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	var id: Long = _

	@BeanProperty
	@Basic(optional = false)
	var token: String = _

	@BeanProperty
	@ManyToOne(optional = false)
	var user: UserEntity = _

	@BeanProperty
	@Basic(optional = false)
	@Temporal(TemporalType.TIMESTAMP)
	var expiration: Date = _

	private def createExpiration() = {
		val now = Calendar.getInstance()
		now.add(Calendar.HOUR, 24)
		now.getTime()
	}

	override def preSave() {
		if (token == null)
			token = RandomStringGenerator.getInstance().randomStringToken(64)

		if (expiration == null)
			expiration = createExpiration()
	}
}