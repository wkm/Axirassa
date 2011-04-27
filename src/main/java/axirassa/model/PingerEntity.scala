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

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class PingerEntity {
	@Id
	@BeanProperty
	@GeneratedValue(strategy = GenerationType.AUTO)
	var id: Long = _

	@BeanProperty
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	var user: UserEntity = _

	@BeanProperty
	@Basic(optional = false)
	var url: String = _

	@BeanProperty
	@Basic(optional = false)
	var frequency: Int = _

	@BeanProperty
	@Basic(optional = false)
	@Enumerated(EnumType.STRING)
	var monitorType: MonitorType = _
}

object PingerEntity {
	def createBroadcastQueueName(userId: Long, pingerId: Long) = {
		"ax.account." + userId + ".pinger." + pingerId
	}
}