
package axirassa.webapp.ajax.httpstream;

import org.cometd.server.AbstractServerTransport.Scheduler;
import org.cometd.server.ServerSessionImpl;
import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationListener;

public class HttpStreamingScheduler implements ContinuationListener, Scheduler {

	// force a new request just before a minute passes (to keep middle-proxies
	// happy)
	public final static int MAX_SESSION_LENGTH = 55000;

	private final ServerSessionImpl serverSession;
	private final Continuation continuation;

	private final HttpStreamingTransportHandler handler;


	public HttpStreamingScheduler(ServerSessionImpl serverSession, Continuation continuation,
	        HttpStreamingTransportHandler handler) {
		this.serverSession = serverSession;
		this.continuation = continuation;
		this.handler = handler;
		continuation.addContinuationListener(this);
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
		continuation.resume();
	}


	@Override
	public void cancel() {
		System.out.println("CANCELING SCHEDULER");

		if (continuation.isSuspended() && !continuation.isExpired())
			continuation.complete();
	}


	@Override
	public void schedule() {
		System.out.println("SCHEDULER SCHEDULED");
		continuation.resume();
	}


	public Continuation getContinuation() {
		return continuation;
	}

}
