
package axirassa.model;

import javax.persistence.Basic;
import javax.persistence.Entity;

@Entity
public class TriggerTemplateEnumerationSettingEntity extends TriggerTemplateSettingEntity {
	@Basic(optional = false)
	private String value;
}
