package axirassa.model

import javax.persistence.Basic
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import scala.reflect.BeanProperty
import org.hibernate.annotations.Cache
import org.hibernate.annotations.CacheConcurrencyStrategy
import javax.persistence.ManyToOne
import axirassa.util.AutoSerializingObject
import axirassa.model.interceptor.EntityPreSave
import axirassa.util.RandomStringGenerator
import org.hibernate.annotations.NaturalId

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class UserEmailAddressEntity extends AutoSerializingObject with EntityPreSave with EntityWithUser {
	//
	// Instance
	//
	@Id
	@BeanProperty
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	var id: Long = _


	@BeanProperty
	@Basic(optional = false)
	var primaryEmail: Boolean = _


	@BeanProperty
	@ManyToOne(optional = false)
	var user: UserEntity = _



	@NaturalId
	@BeanProperty
	@Basic(optional = false)
	var email: String = _


	@BeanProperty
	@Basic(optional = false)
	var token: String = _

	@BeanProperty
	@Basic(optional = false)
	var verified: Boolean = _


	override
	def preSave () {
		if (token == null)
			token = RandomStringGenerator.makeRandomStringToken(36)
	}
}