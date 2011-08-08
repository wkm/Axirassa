
package axirassa.services.pinger;

import java.util.LinkedList;

public class PingerServiceCoordinator {
	private LinkedList<PingerServiceCoordinationMessage> queue = new LinkedList<PingerServiceCoordinationMessage>();


	synchronized public void append(PingerServiceCoordinationMessage message) {
		queue.addLast(message);
	}


	synchronized public void prepend(PingerServiceCoordinationMessage message) {
		queue.addFirst(message);
	}
	
	synchronized public int size() {
		return queue.size();
	}
	
	synchronized public PingerServiceCoordinationMessage peek() {
		return queue.getFirst();
	}
	
	synchronized public PingerServiceCoordinationMessage pollFirst() {
		return queue.pollFirst();
	}


	synchronized public boolean isEmpty() {
	    return (queue.size() == 0)	;
    }

}
