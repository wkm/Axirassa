
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

	public MonitorTypeModel() {}
	public MonitorTypeModel(PingerModel pinger, MonitorType type) {
		this.pinger = pinger;
		this.type = type;
	}
	
	@Id
	@Basic
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "type_id")
	private Long id;

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private PingerModel pinger;

	@Basic
	@Column(name = "type")
	private MonitorType type;

}
