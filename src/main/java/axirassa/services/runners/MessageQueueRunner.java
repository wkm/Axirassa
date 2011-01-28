
package axirassa.services.runners;

import javax.jms.Queue;

/**
 * A service runner that listens to a message queue, taking messages as they
 * become available and giving them to the service.
 * 
 * @author wiktor
 */
public class MessageQueueRunner implements ServiceRunner {

	private final Queue queue;


	public MessageQueueRunner(Queue queue) {
		this.queue = queue;
	}


	@Override
	public void run() throws Exception {

	}

}
