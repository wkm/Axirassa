package axirassa.model

import javax.persistence.Column
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

/**
 * Represents a setting for a Trigger. These are generally hard-coded into the
 * database, that is, they're set by developers, not by users.
 *
 * It's possible to re-do these settings through XML or an enum, etc., but the
 * database system already exists nicely.
 *
 * @author wiktor
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
class TriggerSettingEntity {

    @Id
    @BeanProperty
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id : Long = _

    @BeanProperty
    @Basic(optional = false)
    var name : String = _

    @BeanProperty
    @Basic(optional = false)
    @Column(name = "type")
    var triggerType : Enumeration = _

    @BeanProperty
    @Basic(optional = true)
    var description : String = _

}