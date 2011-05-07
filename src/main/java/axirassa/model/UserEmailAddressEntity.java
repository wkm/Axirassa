
package axirassa.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalId;

import axirassa.model.interceptor.EntityPreSave;
import axirassa.util.AutoSerializingObject;
import axirassa.util.RandomStringGenerator;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserEmailAddressEntity extends AutoSerializingObject implements EntityPreSave, EntityWithUser {
	private static final long serialVersionUID = 4776151084882073597L;

	//
	// Instance
	//
	@Id
	@Getter
	@Setter
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Getter
	@Setter
	@Basic(optional = false)
	private boolean primaryEmail;

	@Getter
	@Setter
	@ManyToOne(optional = false)
	private UserEntity user;

	@NaturalId
	@Getter
	@Setter
	@Basic(optional = false)
	private String email;

	@Getter
	@Setter
	@Basic(optional = false)
	private String token;

	@Getter
	@Setter
	@Basic(optional = false)
	private boolean verified;


	@Override
	public void preSave() {
		if (token == null)
			token = RandomStringGenerator.makeRandomStringToken(36);
	}
}
