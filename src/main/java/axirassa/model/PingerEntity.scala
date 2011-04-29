package axirassa.model

import axirassa.util.AutoSerializingObject
import org.hibernate.annotations.Type
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
class PingerEntity extends EntityWithUser with AutoSerializingObject {
    @Id
    @BeanProperty
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id : Long = _

    @BeanProperty
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    var user : UserEntity = _

    @BeanProperty
    @Basic(optional = false)
    var url : String = _

    @BeanProperty
    @Basic(optional = false)
    var frequency : Int = _

    @BeanProperty
    @Basic(optional = false)
    @Type(`type` = "axirassa.model.MonitorType")
    var monitorType : Enumeration = _
}

object PingerEntity {
    def createBroadcastQueueName(userId : Long, pingerId : Long) = {
        "ax.account."+userId+".pinger."+pingerId
    }
}