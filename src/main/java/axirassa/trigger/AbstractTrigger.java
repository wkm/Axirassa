
package axirassa.trigger;

import lombok.Getter;
import lombok.Setter;
import axirassa.model.PingerEntity;

public class AbstractTrigger implements Trigger {
	@Getter
	@Setter
	private PingerEntity pinger;
}
