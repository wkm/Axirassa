
package axirassa.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AccountEntity implements Serializable {
	private static final long serialVersionUID = -6937561064726878987L;

	// ID
	private Long id;


	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId () {
		return id;
	}


	public void setId (Long id) {
		this.id = id;
	}


	// Name
	private String name;


	@Basic(optional = false)
	public String getName () {
		return name;
	}


	public void setName (String name) {
		this.name = name;
	}

}
