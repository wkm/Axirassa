
package axirassa.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Represents a setting for a Trigger. These are generally hard-coded into the
 * database, that is, they're set by developers, not by users.
 * 
 * It's possible to re-do these settings through XML or an enum, etc., but the
 * database system already exists nicely.
 * 
 * @author wiktor
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class TriggerSettingEntity {
	@Id
	@Getter
	@Setter
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Getter
	@Setter
	@Basic(optional = false)
	private String name;

	@Getter
	@Setter
	@Basic(optional = false)
	private TriggerSettingType type;

	
	@Getter
	@Setter
	@Basic(optional = true)
	private String description;
}
