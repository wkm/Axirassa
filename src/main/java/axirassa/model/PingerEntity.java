
package axirassa.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Pingers")
public class PingerEntity implements Serializable {
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


	// USER
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private UserEntity user;


	public UserEntity getUser() {
		return user;
	}


	public void setUser(UserEntity user) {
		this.user = user;
	}


	// URL
	@Basic(optional = false)
	@Column(name = "URL")
	private String url;


	public void setUrl(String url) {
		this.url = url;
	}


	public String getUrl() {
		return url;
	}


	/**
	 * The number of seconds between checks
	 */
	@Basic(optional = false)
	@Column(name = "frequency")
	private int frequency;


	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}


	public void setFrequency(PingerFrequency frequency) {
		this.frequency = frequency.getInterval();
	}


	public int getFrequency() {
		return frequency;
	}


	@OneToMany(fetch = FetchType.EAGER)
	public Set<MonitorTypeEntity> monitorType;


	public Set<MonitorTypeEntity> getMonitorType() {
		return monitorType;
	}


	public void setMonitorType(Set<MonitorTypeEntity> monitorType) {
		this.monitorType = monitorType;
	}
}
