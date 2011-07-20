
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
import axirassa.messaging.VoiceRequestMessage;
import axirassa.messaging.util.CommonBackoffStrategies;
import axirassa.messaging.util.ExponentialBackoffStrategy;
import axirassa.messaging.util.InfiniteLoopExceptionSurvivor;
import axirassa.services.Service;
import axirassa.util.MessagingTools;

@Slf4j
public class VoiceNotificationService implements Service {
	private final ClientSession messaging;
	private final HttpClient httpClient;


	public VoiceNotificationService(ClientSession messagingSession) {
		this.messaging = messagingSession;
		this.httpClient = new DefaultHttpClient();
	}


	@Override
	public void execute() throws Throwable {
		messaging.start();

		final ClientConsumer consumer = messaging.createConsumer(Messaging.NOTIFY_VOICE_REQUEST);

		InfiniteLoopExceptionSurvivor executor = new InfiniteLoopExceptionSurvivor(
		        ExponentialBackoffStrategy.clone(CommonBackoffStrategies.EXPONENTIAL_BACKOFF_MESSAGING),
		        new Callable<Object>() {
			        @Override
			        public Object call() throws Exception {
				        ClientMessage message = consumer.receive();
				        System.out.println("####\n####\n####\n####\n####\n####\n");
				        System.out.println("RECIEVED MESSAGE: " + message);

				        Object rawobject = MessagingTools.fromMessageBytes(message);
				        if (rawobject instanceof VoiceRequestMessage) {
					        VoiceRequestMessage voiceRequest = (VoiceRequestMessage) rawobject;
					        String text = PhoneTemplateFactory.instance.getText(voiceRequest.getTemplate(),
					                                                            PhoneTemplateType.VOICE,
					                                                            voiceRequest.getAttributeMap());

					        SendVoice sender = new SendVoice(voiceRequest.getPhoneNumber(),
					                voiceRequest.getExtension(), text);
					        sender.send(httpClient);
				        }

				        message.acknowledge();
				        messaging.commit();
				        return null;
			        }
		        }, new Function1<Object, Throwable>() {
			        @Override
			        public Object call(Throwable e) throws Throwable {
				        if (e instanceof ClassNotFoundException)
					        log.error("Exception: ", e);
				        else
					        throw e;

				        return null;
			        }
		        });
		
		executor.execute();
	}
}
