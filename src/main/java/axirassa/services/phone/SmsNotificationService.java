
package axirassa.services.phone;

import java.util.concurrent.Callable;

import lombok.extern.slf4j.Slf4j;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientMessage;
import org.hornetq.api.core.client.ClientSession;

import zanoccio.javakit.lambda.Function1;
import axirassa.config.Messaging;
import axirassa.messaging.SmsRequestMessage;
import axirassa.messaging.util.CommonBackoffStrategies;
import axirassa.messaging.util.InfiniteLoopExceptionSurvivor;
import axirassa.services.Service;
import axirassa.util.MessagingTools;

@Slf4j
public class SmsNotificationService implements Service {
	private final ClientSession messagingSession;
	private final HttpClient httpClient;


	public SmsNotificationService(ClientSession messagingSession) {
		this.messagingSession = messagingSession;
		this.httpClient = new DefaultHttpClient();
	}


	@Override
	public void execute() throws Throwable {
		messagingSession.start();

		final ClientConsumer consumer = messagingSession.createConsumer(Messaging.NOTIFY_SMS_REQUEST);

		InfiniteLoopExceptionSurvivor executor = new InfiniteLoopExceptionSurvivor(
		        CommonBackoffStrategies.EXPONENTIAL_BACKOFF_MESSAGING(),
		        new Callable<Object>() {
			        @Override
			        public Object call() throws Exception {
				        ClientMessage message = consumer.receive();
				        SmsRequestMessage smsRequest = MessagingTools
				                .fromMessageBytes(SmsRequestMessage.class, message);

				        String text = PhoneTemplateFactory.instance.getText(smsRequest.getTemplate(),
				                                                            PhoneTemplateType.SMS,
				                                                            smsRequest.getAttributeMap());

				        SendSMS sender = new SendSMS(smsRequest.getPhoneNumber(), text);
				        sender.send(httpClient);

				        message.acknowledge();
				        messagingSession.commit();

				        return null;
			        }

		        }, new Function1<Boolean, Throwable>() {
			        public Boolean call(Throwable e) {
				        if (e instanceof ClassNotFoundException)
					        log.error("Exception: ", e);
				        return null;
			        }
		        });

		executor.execute();
	}
}
