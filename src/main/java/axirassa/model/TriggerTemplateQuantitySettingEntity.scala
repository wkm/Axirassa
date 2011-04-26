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
class TriggerTemplateQuantitySettingEntity extends TriggerTemplateSettingEntity {
	@BeanProperty
	@Basic(optional = false)
	var quantity: Int = _
}