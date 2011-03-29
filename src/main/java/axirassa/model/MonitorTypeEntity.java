
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
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class MonitorTypeEntity implements Serializable {
	private static final long serialVersionUID = -2890867580447534180L;


	public MonitorTypeEntity () {
		// simple pojo
	}


	public MonitorTypeEntity (MonitorType type) {
		this.setType(type);
	}


	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;


	public void setId (Long id) {
		this.id = id;
	}


	public Long getId () {
		return id;
	}


	@Basic(optional = false)
	private MonitorType type;


	public void setType (MonitorType type) {
		this.type = type;
	}


	public MonitorType getType () {
		return type;
	}

}
