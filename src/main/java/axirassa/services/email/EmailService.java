
package axirassa.services.email;

import java.io.IOException;

import org.apache.http.impl.client.DefaultHttpClient;
import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientSession;
import org.hornetq.utils.json.JSONException;

import axirassa.config.Messaging;
import axirassa.messaging.EmailRequestMessage;
import axirassa.services.Service;
import axirassa.util.MessagingTools;
import freemarker.template.TemplateException;

public class EmailService implements Service {
	private final ClientSession messagingSession;
	private final DefaultHttpClient httpClient = new DefaultHttpClient();


	public EmailService(ClientSession messagingSession) {
		this.messagingSession = messagingSession;
	}


	@Override
	public void execute() throws IllegalStateException, IOException, HornetQException {
		messagingSession.start();

		ClientConsumer consumer = messagingSession.createConsumer(Messaging.NOTIFY_EMAIL_REQUEST);


		while (true) {
			try {
				ClientMessage message = consumer.receive();

				System.out.println("Received message: " + message);
				Object rawobject = MessagingTools.fromMessageBytes(message);

				if (rawobject instanceof EmailRequestMessage) {
					EmailRequestMessage emailRequest = (EmailRequestMessage) rawobject;

					EmailTemplateComposer composer = new EmailTemplateComposer(emailRequest.getTemplate());
					composer.setAttributes(emailRequest.getAttributeMap());

					EmailSender sender = new EmailSender(composer, emailRequest.getToAddress());
					sender.send(httpClient);
				}

				message.acknowledge();
				messagingSession.commit();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (TemplateException e) {
				e.printStackTrace();
			}
		}
	}
}
