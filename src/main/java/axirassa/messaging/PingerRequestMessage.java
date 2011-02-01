
package axirassa.messaging;

import java.util.ArrayList;
import java.util.Collection;

import axirassa.model.MonitorType;
import axirassa.model.MonitorTypeEntity;
import axirassa.model.PingerEntity;
import axirassa.util.AutoSerializingObject;

public class PingerRequestMessage extends AutoSerializingObject {
	private static final long serialVersionUID = 656890899165340552L;

	private final String url;
	private final Collection<MonitorType> types;


	public PingerRequestMessage(PingerEntity pinger) {
		url = pinger.getUrl();

		types = new ArrayList<MonitorType>(pinger.getMonitorType().size());
		for (MonitorTypeEntity type : pinger.getMonitorType())
			types.add(type.getType());
	}


	public String getUrl() {
		return url;
	}


	public Collection<MonitorType> getType() {
		return types;
	}
}
