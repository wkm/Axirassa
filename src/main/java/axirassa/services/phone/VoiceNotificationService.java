
package axirassa.services.phone;

import org.hornetq.api.core.client.ClientSession;

import axirassa.services.Service;

public class VoiceNotificationService implements Service {
	private final ClientSession messagingSession;


	public VoiceNotificationService(ClientSession messagingSession) {
		this.messagingSession = messagingSession;
	}


	@Override
	public void execute() throws Exception {
		System.err.println("EXECUTING VOICE NOTIFICATION SERVICE");
	}
}
