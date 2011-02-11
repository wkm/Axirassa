
package axirassa.webapp.ajax.httpstream;

import org.cometd.server.AbstractServerTransport.Scheduler;
import org.cometd.server.ServerSessionImpl;
import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationListener;

public class HttpStreamingScheduler implements ContinuationListener, Scheduler {

	private final ServerSessionImpl serverSession;
	private final Continuation continuation;


	public HttpStreamingScheduler(ServerSessionImpl serverSession, Continuation continuation) {
		this.serverSession = serverSession;
		this.continuation = continuation;
	}


	public ServerSessionImpl getServerSession() {
		return serverSession;
	}


	//
	// the general strategy here is to stop the request and let the client
	// reconnect
	//

	@Override
	public void onComplete(Continuation continuation) {
		System.out.println("CONTINUATION COMPLETE");
		// nothing to do
	}


	@Override
	public void onTimeout(Continuation continuation) {
		System.out.println("CONTINUATION TIMEOUT");
		// remove the scheduler, the client has to reconnect
		serverSession.setScheduler(null);
	}


	@Override
	public void cancel() {
		System.out.println("CANCELING SCHEDULER");

	}


	@Override
	public void schedule() {
		System.out.println("SCHEDULER SCHEDULED");
		continuation.resume();
	}

}
