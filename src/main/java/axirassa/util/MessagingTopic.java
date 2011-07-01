
package axirassa.util;

import lombok.extern.slf4j.Slf4j;

import org.hornetq.api.core.HornetQException;
import org.hornetq.api.core.client.ClientConsumer;
import org.hornetq.api.core.client.ClientSession;

/**
 * A gentle wrapper around creating a temporary queue subscribed to a given
 * address.
 * 
 * This effectively emulates JMS topics, but with the distinct benefit that
 * topics may be created programmatically.
 * 
 * @author wiktor
 */
@Slf4j
public class MessagingTopic {

	private final ClientSession session;
	private String topicQueue;
	private final String topicName;


	public MessagingTopic (ClientSession session, String topicName) throws HornetQException {
		this.session = session;
		this.topicName = topicName;
		session.createTemporaryQueue(topicName, getTopicQueueName());

		log.debug("Created queue: " + topicQueue);
		log.debug("\tsubscribed to: " + topicName);
	}


	private String getTopicQueueName () {
		if (topicQueue == null)
			createTopicQueueName();

		return topicQueue;
	}


	private void createTopicQueueName () {
		topicQueue = "topic_" + RandomStringGenerator.makeRandomStringToken(30) + "_" + topicName;
	}


	public ClientConsumer createConsumer () throws HornetQException {
		return session.createConsumer(topicQueue);
	}


	/**
	 * Explicitly deletes the temporary queue created when a MessagingTopic was
	 * instantiated.
	 */
	public void unsubscribe () throws HornetQException {
		session.deleteQueue(topicQueue);
	}
}
