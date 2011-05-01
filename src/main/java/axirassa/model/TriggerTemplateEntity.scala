package axirassa.model

import scala.reflect.BeanProperty

import javax.persistence.Basic
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne

import lombok.Getter
import lombok.Setter

import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class TriggerTemplateEntity {
  @Id
  @BeanProperty
  @Basic(optional = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  var id : Long = _

  @BeanProperty
  @Basic(optional = false)
  var name : String = _

  @BeanProperty
  @ManyToOne(optional = false)
  var user : UserEntity = _
}