
package axirassa.webapp.ajax.httpstream;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cometd.bayeux.Channel;
import org.cometd.bayeux.server.ServerMessage;
import org.cometd.server.BayeuxServerImpl;
import org.cometd.server.ServerSessionImpl;
import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;

public class HttpStreamingTransportHandler {

	private final static String USER_AGENT_HEADER = "User-Agent";
	private final static String SCHEDULER_ATTRIBUTE = "cometd.httpstreaming.scheduler";
	private static final String REQUEST_TICK_ATTRIBUTE = "cometd.httpstreaming.requesttick";
	private static final long JETTY_TIMEOUT_BUFFER = 30 * 1000;

	private final HttpServletRequest request;
	private final HttpServletResponse response;
	private ServerSessionImpl serverSession;
	private final HttpStreamingTransport transport;
	private JSONStreamPrintWriter writer;
	private long requestStartTick = System.nanoTime();


	public HttpStreamingTransportHandler (HttpStreamingTransport transport, HttpServletRequest request,
	        HttpServletResponse response) {
		this.transport = transport;
		this.request = request;
		this.response = response;
	}


	private void info (Object... args) {
		Object[] withPrefix = new Object[args.length + 2];
		withPrefix[0] = Long.toHexString(hashCode());
		withPrefix[1] = "   ";

		for (int i = 0; i < args.length; i++)
			withPrefix[i + 2] = args[i];

		transport.info(withPrefix);
	}


	private BayeuxServerImpl getBayeux () {
		return transport.getBayeux();
	}


	public void handle () {
		Object schedulerAttribute = request.getAttribute(SCHEDULER_ATTRIBUTE);
		info("schdulerAttribute: ", schedulerAttribute);

		Continuation continuation = ContinuationSupport.getContinuation(request);
		// continuation states
		System.err.println("CONTINUATION STATES:");
		if (continuation.isInitial())
			System.err.println("\t >>>> initial");
		if (continuation.isExpired())
			System.err.println("\t >>>> expired");
		if (continuation.isResumed())
			System.err.println("\t >>>> resumed");
		if (continuation.isSuspended())
			System.err.println("\t >>>> suspended");
		System.err.println("\n");

		if (schedulerAttribute == null) {
			info("NO SCHEDULER");
			// the session doesn't have any scheduler associated with it, so it
			// must be new
			handleNewSession();
		} else {
			info("HANDLING RESUME");
			if (!(schedulerAttribute instanceof HttpStreamingScheduler)) {
				info("DANGER DANGER: scheduler attribute not a HttpStreamingScheduler");
				return;
			}

			Object tick = request.getAttribute(REQUEST_TICK_ATTRIBUTE);
			if (tick == null) {
				info("NO REQUEST TICK");

				// force the session to immediately reconnect
				requestStartTick = 0;
				return;
			} else {
				if ((tick instanceof Long))
					requestStartTick = (Long) tick;
				else {
					info("REQUEST TICK OF WRONG TYPE");
					requestStartTick = 0;
				}
			}

			HttpStreamingScheduler scheduler = (HttpStreamingScheduler) schedulerAttribute;
			handleResumedSession(scheduler);
		}
	}


	public void handleResumedSession (HttpStreamingScheduler scheduler) {
		serverSession = scheduler.getServerSession();

		if (serverSession.isConnected())
			serverSession.startIntervalTimeout();

		sendQueuedMessages();
		suspendSession();
	}


	public void handleNewSession () {
		try {
			ServerMessage.Mutable[] messages = transport.parseRequestMessages(request);
			if (messages == null) {
				info("no messages");
				return;
			}

			response.setContentType("application/json");

			serverSession = null;

			// padInitialReply();

			// process each message
			for (ServerMessage.Mutable message : messages)
				serverSession = processMessage(message);

			if (messages.length > 1)
				suspendSession();
		} catch (ParseException e) {
			info("ERROR PARSING JSON: " + e.getMessage(), e.getCause());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
	}


	private ServerSessionImpl processMessage (ServerMessage.Mutable message) {
		boolean isConnectMessage = Channel.META_CONNECT.equals(message.getChannel());

		String clientId = message.getClientId();
		if (serverSession == null || isClientSessionMismatch(clientId, serverSession)) {
			serverSession = retrieveSession(clientId);
		} else if (!serverSession.isHandshook()) {
			serverSession = null;
		}

		ServerMessage.Mutable reply = getBayeux().handle(serverSession, message);
		if (reply != null) {
			boolean isHandshake = false;
			if (serverSession == null) {
				isHandshake = true;
				handleHandshake(reply);
			} else {
				info("#### NOT A HANDSHAKE");

				if (isConnectMessage || !isMetaConnectDeliveryOnly())
					sendQueuedMessages();
			}

			// TODO not totally sure what this does, it appears to propagate
			// /meta/* messages (see #extendSend)
			reply = getBayeux().extendReply(serverSession, serverSession, reply);
			sendReply(reply);

			if (isHandshake) {
				// immediately finish
				writer.close();
				info(" >>>> [closed]");
			}

			message.setAssociated(null);
		}

		return serverSession;
	}


	private void suspendSession () {
		info("SUSPENDING SESSION");
		long timeout = computeTimeout();
		info("TIMEOUT FOR: ", timeout);

		if (timeout > 0) {
			Continuation continuation = ContinuationSupport.getContinuation(request);
			continuation.setTimeout(timeout + JETTY_TIMEOUT_BUFFER);

			HttpStreamingScheduler scheduler = (HttpStreamingScheduler) serverSession.getAttribute("SCHEDULER");
			if (scheduler == null) {
				System.err.println("!!!!! CREATING NEW SCHEDULER");
				scheduler = new HttpStreamingScheduler(serverSession, continuation, this);
				serverSession.setTimeout(timeout);
				serverSession.setScheduler(scheduler);

				serverSession.setAttribute("SCHEDULER", scheduler);
			} else {
				System.err.println("!!!!! SCHEDULER ALREADY EXISTS");
				serverSession.setTimeout(timeout);
			}

			request.setAttribute(SCHEDULER_ATTRIBUTE, scheduler);
			request.setAttribute(REQUEST_TICK_ATTRIBUTE, requestStartTick);

			continuation.suspend(response);

			info("serverSession timeout: ", serverSession.getTimeout(), "  interval: ", serverSession.getInterval());
		} else {
			Continuation continuation = ContinuationSupport.getContinuation(request);
			continuation.suspend(response);
			continuation.complete();
		}
	}


	private long computeTimeout () {
		long baseTimeout = transport.getTimeout();

		long delta = (System.nanoTime() - requestStartTick) / 1000000;
		if (delta > baseTimeout)
			return 0;

		if (delta < 0)
			return baseTimeout;

		return baseTimeout - delta;
	}


	private void sendQueuedMessages () {
		if (serverSession != null) {
			final List<ServerMessage> queue = serverSession.takeQueue();
			for (ServerMessage message : queue) {
				sendReply(message);
			}
		}
	}


	private boolean isMetaConnectDeliveryOnly () {
		return transport.isMetaConnectDeliveryOnly() || serverSession.isMetaConnectDeliveryOnly();
	}


	private void sendReply (ServerMessage reply) {
		if (reply == null)
			return;
		//
		// Map<String, Object> adviceFields = reply.asMutable().getAdvice(true);
		//
		// adviceFields.put(Message.INTERVAL_FIELD, computeTimeout());
		// if (serverSession != null)
		// serverSession.reAdvise();

		writeMessage(reply.getJSON());
	}


	/**
	 * send some white space down the front of the reply to get the continuation
	 * into a non-inital state.
	 */
	private void padInitialReply () {
		try {
			info("PADDED INTIAL REPLY");
			response.getWriter().write("                        ");
		} catch (IOException e) {
			System.out.println("Could not write message: ");
			e.printStackTrace(System.err);
		}
	}


	private void writeMessage (String message) {
		if (message == null)
			return;

		try {
			if (writer == null)
				writer = new JSONStreamPrintWriter(response.getWriter());

			writer.write(message);
			info(" >>>> ", message);

			flushResponse();
		} catch (IOException e) {
			System.out.println("Could not successfully write message: ");
			e.printStackTrace(System.err);
		}
	}


	private void finishResponse () {
		if (writer == null)
			return;

		writer.close();
	}


	private void flushResponse () {
		if (writer == null)
			return;

		try {
			writer.flush();
			response.flushBuffer();
			info(" >>>> [flushed]");
		} catch (IOException e) {
			info("Could not flush HTTP response buffer: ", e);
		}
	}


	private void handleHandshake (ServerMessage reply) {
		info("handling handshake");
		serverSession = retrieveSession(reply.getClientId());

		// get the user agent
		if (serverSession != null)
			serverSession.setUserAgent(request.getHeader(USER_AGENT_HEADER));
	}


	/**
	 * @return the ServerSession associated with the given clientId, null if no
	 *         such sesion exists
	 */
	private ServerSessionImpl retrieveSession (String clientId) {
		return (ServerSessionImpl) getBayeux().getSession(clientId);
	}


	/**
	 * @return true if the given clientId does not match the given
	 *         serverSession.
	 */
	private boolean isClientSessionMismatch (String clientId, ServerSessionImpl serverSession) {
		return clientId != null && !clientId.equals(serverSession.getId());
	}
}
