
package axirassa.domain;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "MonitorTypes")
public class MonitorTypeModel implements Serializable {
	private static final long serialVersionUID = -2890867580447534180L;


	public MonitorTypeModel() {
	}


	public MonitorTypeModel(PingerModel pinger, MonitorType type) {
		this.setPinger(pinger);
		this.setType(type);
	}


	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "type_id")
	private Long id;


	public void setId(Long id) {
		this.id = id;
	}


	public Long getId() {
		return id;
	}


	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private PingerModel pinger;


	public void setPinger(PingerModel pinger) {
		this.pinger = pinger;
	}


	public PingerModel getPinger() {
		return pinger;
	}


	@Basic(optional = false)
	@Column(name = "type")
	private MonitorType type;


	public void setType(MonitorType type) {
		this.type = type;
	}


	public MonitorType getType() {
		return type;
	}

}
