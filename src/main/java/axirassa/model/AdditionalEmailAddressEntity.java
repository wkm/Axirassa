
package axirassa.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import axirassa.model.interceptor.EntityPreSave;
import axirassa.util.AutoSerializingObject;

@Entity
@Table(name = "AdditionalEmailAddresses")
public class AdditionalEmailAddressEntity extends AutoSerializingObject implements EntityPreSave, EntityWithUser {

	//
	// Instance
	//
	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	@Override
	public UserEntity getUser() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void preSave() {
		// TODO Auto-generated method stub

	}

}
