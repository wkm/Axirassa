
package axirassa.model;

import javax.persistence.Basic;
import javax.persistence.Entity;

@Entity
public class TriggerTemplateQuantitySettingEntity extends TriggerTemplateSettingEntity {
	@Basic(optional = false)
	private int quantity;
}
