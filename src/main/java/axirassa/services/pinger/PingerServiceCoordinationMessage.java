package axirassa.services.pinger;

import lombok.Getter;
import lombok.Setter;

import org.hornetq.api.core.client.ClientMessage;

import axirassa.model.HttpStatisticsEntity;

public class PingerServiceCoordinationMessage {
	public static PingerServiceCoordinationMessage shutdownMessage() {
		PingerServiceCoordinationMessage message = new PingerServiceCoordinationMessage();
		message.setShutdownMessage(true);
		return message;
	}
 

	public static PingerServiceCoordinationMessage pingMessage(ClientMessage clientMessage) {
		PingerServiceCoordinationMessage message = new PingerServiceCoordinationMessage();
		message.setClientMessage(clientMessage);
		return message;
	}


	@Setter
	@Getter
	private boolean shutdownMessage = false;

	@Setter
	@Getter
	private ClientMessage clientMessage = null;
	
	@Setter
	@Getter
	private HttpStatisticsEntity statistic = null;
}
