package axirassa.model

import javax.persistence.Basic
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import scala.reflect.BeanProperty
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import javax.persistence.FetchType
import javax.persistence.Enumerated
import javax.persistence.EnumType
import javax.persistence.OneToOne
import org.hibernate.annotations.Cascade
import org.hibernate.annotations.CascadeType

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class AccountUserEntity {

    @Id
    @BeanProperty
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id : Long = _

    @BeanProperty
    @OneToOne(targetEntity = classOf[AccountEntity], fetch = FetchType.LAZY)
    @Cascade(Array(CascadeType.PERSIST))
    var account : AccountEntity = _

    @BeanProperty
    @OneToOne(targetEntity = classOf[UserEntity], fetch = FetchType.LAZY)
    @Cascade(Array(CascadeType.PERSIST))
    var user : UserEntity = _

    @BeanProperty
    @Basic(optional = false)
    @Enumerated(EnumType.STRING)
    var role : Enumeration = _

}