
package axirassa.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import axirassa.model.interceptor.EntityPreSave;

/**
 * @author wiktor
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONE)
public class FeedbackEntity implements Serializable, EntityPreSave {
	private static final long serialVersionUID = 1535970389033283812L;

	//
	// INSTANCE
	//

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;


	public Long getId () {
		return id;
	}


	public void setId (Long id) {
		this.id = id;
	}


	@Basic(optional = false)
	private String comment;


	public String getComment () {
		return comment;
	}


	public void setComment (String comment) {
		this.comment = comment;
	}


	@Basic(optional = false)
	private String useragent;


	public String getUseragent () {
		return useragent;
	}


	public void setUseragent (String useragent) {
		this.useragent = useragent;
	}


	@ManyToOne(optional = true)
	private UserEntity user;


	public UserEntity getUser () {
		return user;
	}


	public void setUser (UserEntity user) {
		this.user = user;
	}


	@Basic(optional = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;


	public Date getDate () {
		return date;
	}


	public void setDate (Date date) {
		this.date = date;
	}


	public String getFormattedDate () {
		SimpleDateFormat format = new SimpleDateFormat("h:m:s a");
		return format.format(getDate());
	}


	@Basic(optional = false)
	private boolean posted;


	public boolean isPosted () {
		return posted;
	}


	public void setPosted (boolean posted) {
		this.posted = posted;
	}


	@Override
	public void preSave () {
		if (date == null)
			date = new Date();
	}
}
