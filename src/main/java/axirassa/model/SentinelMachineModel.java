
package axirassa.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "SentinelMachines")
public class SentinelMachineModel implements Serializable {
	private static final long serialVersionUID = -4248011777749278671L;

	// ID

	@Id
	@Basic
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	//
	// name
	//

	@Basic(optional = false)
	private String name;


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

}
