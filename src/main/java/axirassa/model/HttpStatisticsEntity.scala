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
@Cache(usage = CacheConcurrencyStrategy.NONE)
class HttpStatisticsEntity extends AutoSerializingObject with Serializable {
	@Id
	@BeanProperty
	@GeneratedValue(strategy = GenerationType.AUTO)
	var id: Long = _

	@Basic
	@BeanProperty
	@Temporal(TemporalType.TIMESTAMP)
	@GeneratedValue(strategy = GenerationType.AUTO)
	var timestamp: Date = _

	@BeanProperty
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	var pinger: PingerEntity = _

	@BeanProperty
	@Basic(optional = false)
	var statusCode: Int = _

	@BeanProperty
	@Basic(optional = false)
	var latency: Int = _

	@BeanProperty
	@Basic(optional = false)
	var responseTime: Int = _

	@BeanProperty
	@Basic(optional = true)
	var responseSize: Long = _

	@BeanProperty
	@Basic(optional = true)
	var uncompressedSize: Long = _

	def getTimestampInMillis() = {
		val calendar = Calendar.getInstance()
		calendar.setTime(getTimestamp())
		calendar.getTimeInMillis()
	}
}