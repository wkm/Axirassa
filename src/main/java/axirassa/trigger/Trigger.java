
package axirassa.trigger;

import axirassa.model.PingerEntity;

public interface Trigger {

	public abstract void setPinger (PingerEntity pinger);


	public abstract PingerEntity getPinger ();

}
