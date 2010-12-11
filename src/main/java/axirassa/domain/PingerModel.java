
package axirassa.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Pingers")
public class PingerModel implements Serializable {
	private static final long serialVersionUID = -6709719920544228167L;

	// ID
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

}
