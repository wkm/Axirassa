
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

import lombok.Getter;
import lombok.Setter;

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
	@Getter
	@Setter
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Getter
	@Setter
	@Basic(optional = false)
	private String comment;


	@Getter
	@Setter
	@Basic(optional = false)
	private String useragent;


	@Getter
	@Setter
	@ManyToOne(optional = true)
	private UserEntity user;

	@Getter
	@Setter
	@Basic(optional = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;


	public String getFormattedDate () {
		SimpleDateFormat format = new SimpleDateFormat("h:m:s a");
		return format.format(getDate());
	}


	@Getter
	@Setter
	@Basic(optional = false)
	private boolean posted;

	@Override
	public void preSave () {
		if (date == null)
			date = new Date();
	}
}
