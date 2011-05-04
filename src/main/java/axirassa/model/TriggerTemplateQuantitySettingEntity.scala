package axirassa.model

import scala.reflect.BeanProperty

import javax.persistence.Basic
import javax.persistence.Entity
@Entity
class TriggerTemplateQuantitySettingEntity extends TriggerTemplateSettingEntity {
	@BeanProperty
	@Basic(optional = false)
	var quantity: Int = _
}