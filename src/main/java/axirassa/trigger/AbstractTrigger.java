
package axirassa.trigger;

import axirassa.model.PingerEntity;

public class AbstractTrigger implements Trigger {
	private PingerEntity pinger;


	/* (non-Javadoc)
     * @see axirassa.trigger.Trigger#setPinger(axirassa.model.PingerEntity)
     */
	@Override
    public void setPinger (PingerEntity pinger) {
		this.pinger = pinger;
	}


	/* (non-Javadoc)
     * @see axirassa.trigger.Trigger#getPinger()
     */
	@Override
    public PingerEntity getPinger () {
		return pinger;
	}
}
