
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
		// nothing to do
	}


	@Override
	public void onTimeout(Continuation continuation) {
		// remove the scheduler, the client has to reconnect
		serverSession.setScheduler(null);
	}


	@Override
	public void cancel() {
		if (continuation.isSuspended() && !continuation.isExpired()) {
			System.out.println("continuation was suspended");

			try {
				continuation.complete();
			} catch (Exception e) {
				// ignore
				System.out.println("Exception during scheduler #cancel: " + e.getMessage());
			}
		}
	}


	@Override
	public void schedule() {
		if (!continuation.isResumed()) {
			continuation.resume();
		}
	}


	public Continuation getContinuation() {
		return continuation;
	}

}
