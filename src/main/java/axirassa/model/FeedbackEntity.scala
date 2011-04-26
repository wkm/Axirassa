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
import java.text.SimpleDateFormat

@Entity
@Cache(usage = CacheConcurrencyStrategy.NONE)
class FeedbackEntity extends Serializable with EntityPreSave {
    @Id
    @BeanProperty
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id : Long = _

    @BeanProperty
    @Basic(optional = false)
    var comment : String = _

    @BeanProperty
    @Basic(optional = false)
    var useragent : String = _

    @BeanProperty
    @ManyToOne(optional = true)
    var user : UserEntity = _

    @BeanProperty
    @Basic(optional = false)
    @Temporal(TemporalType.TIMESTAMP)
    var date : Date = _

    def getFormattedDate() = {
        val format = new SimpleDateFormat("h:m:s a")
        format.format(getDate())
    }

    @BeanProperty
    @Basic(optional = false)
    var posted : Boolean = _

    override def preSave() {
        if (date == null)
            date = new Date()
    }
}