
package axirassa.domain;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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


	// USER
	@ManyToOne(fetch = FetchType.LAZY)
	private UserModel user;


	public UserModel getUser() {
		return user;
	}


	public void setUser(UserModel user) {
		this.user = user;
	}


	// URL
	@Basic
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
	@Basic
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
}
