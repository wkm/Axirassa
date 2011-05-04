
package axirassa.util;



import org.hornetq.api.core.client.ClientSession;

import org.slf4j.LoggerFactory;

/**
 * A gentle wrapper around creating a temporary queue subscribed to a given
 * address.
 *
 * This effectively emulates JMS topics, but with the distinct benefit that
 * topics may be created programmatically.
 *
 * @author wiktor
 */
class MessagingTopic(session : ClientSession, topicName : String) {
  private val log = LoggerFactory.getLogger(classOf[MessagingTopic]);

  var topicQueueName : String = _
  session.createTemporaryQueue(topicName, topicQueueName);

  log.debug("Created queue: "+topicQueueName);
  log.debug("\tsubscribed to: "+topicName);

  private def getTopicQueueName() = {
    if (topicQueueName == null)
      createTopicQueueName();

    topicQueueName
  }

  private def createTopicQueueName() {
    topicQueueName = "topic_"+RandomStringGenerator.makeRandomStringToken(30)+"_"+topicName;
  }

  def createConsumer() = session.createConsumer(topicQueueName)

  /**
   * Explicitly deletes the temporary queue created when a MessagingTopic was
   * instantiated.
   */
  def unsubscribe() {
    session.deleteQueue(topicQueueName);
  }
}
