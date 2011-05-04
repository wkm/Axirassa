package axirassa.model

import scala.reflect.BeanProperty

import javax.persistence.Basic
import javax.persistence.Entity
@Entity
class TriggerTemplateEnumerationSettingEntity extends TriggerTemplateSettingEntity {
  @BeanProperty
  @Basic(optional = false)
  var value : String = _
}