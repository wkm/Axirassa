
package axirassa.webapp.ajax.httpstream;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cometd.bayeux.Channel;
import org.cometd.bayeux.Message;
import org.cometd.bayeux.server.ServerMessage;
import org.cometd.server.BayeuxServerImpl;
import org.cometd.server.ServerSessionImpl;
import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;

public class HttpStreamingTransportHandler {

	private final static String USER_AGENT_HEADER = "User-Agent";
	private final static String SCHEDULER_ATTRIBUTE = "cometd.httpstreaming.scheduler";
	private static final String REQUEST_TICK_ATTRIBUTE = "cometd.httpstreaming.requesttick";

	private final HttpServletRequest request;
	private HttpServletResponse response;
	private ServerSessionImpl serverSession;
	private final HttpStreamingTransport transport;
	private JSONStreamPrintWriter writer;
	private long requestStartTick = System.nanoTime();


	public HttpStreamingTransportHandler(HttpStreamingTransport transport, HttpServletRequest request,
	        HttpServletResponse response) {
		this.transport = transport;
		this.request = request;
		this.response = response;
	}


	private void info(Object... args) {
		Object[] withPrefix = new Object[args.length + 2];
		withPrefix[0] = Long.toHexString(hashCode());
		withPrefix[1] = "   ";
		for (int i = 0; i < args.length; i++)
			withPrefix[i + 2] = args[i];

		transport.info(withPrefix);
	}


	private BayeuxServerImpl getBayeux() {
		return transport.getBayeux();
	}


	public void handle() {
		System.out.println("\n\n\n\n\n\n");
		Object schedulerAttribute = request.getAttribute(SCHEDULER_ATTRIBUTE);
		info("schdulerAttribute: ", schedulerAttribute);

		if (schedulerAttribute == null) {
			info("NO SCHEDULER");
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
			info("ContentType: ", response.getContentType());

			response = (HttpServletResponse) scheduler.getContinuation().getServletResponse();
			info("OtherContentType: ", response.getContentType());

			handleResumedSession(scheduler);
		}
	}


	public void handleResumedSession(HttpStreamingScheduler scheduler) {
		info("HANDLING RESUMED SESSION");
		serverSession = scheduler.getServerSession();
		if (serverSession.isConnected()) {
			info("starting serverSession interval timeout");
			serverSession.startIntervalTimeout();
		}

		sendQueuedMessages();
		suspendSession();
	}


	public void handleNewSession() {
		try {
			ServerMessage.Mutable[] messages = transport.parseRequestMessages(request);
			if (messages == null) {
				info("no messages");
				return;
			}

			response.setContentType("application/json");

			serverSession = null;

			// process each message
			for (ServerMessage.Mutable message : messages) {
				serverSession = processMessage(message);
			}
		} catch (ParseException e) {
			info("ERROR PARSING JSON: " + e.getMessage(), e.getCause());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
		}
	}


	private ServerSessionImpl processMessage(ServerMessage.Mutable message) {
		boolean isConnectMessage = Channel.META_CONNECT.equals(message.getChannel());

		String clientId = message.getClientId();
		if (serverSession == null || isClientSessionMismatch(clientId, serverSession)) {
			serverSession = retrieveSession(clientId);
		} else if (!serverSession.isHandshook()) {
			serverSession = null;
		}

		ServerMessage reply = getBayeux().handle(serverSession, message);
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
			} else {
				suspendSession();
			}

			message.setAssociated(null);
		}

		// info("CLOSING WRITER TO FINISH SENDING");
		// finishResponse();

		return serverSession;
	}


	private void suspendSession() {
		info("SUSPENDING SESSION");
		long timeout = computeTimeout();
		info("TIMEOUT FOR: ", timeout);

		if (timeout > 0) {
			Continuation continuation = ContinuationSupport.getContinuation(request);
			continuation.setTimeout(timeout);

			HttpStreamingScheduler scheduler = new HttpStreamingScheduler(serverSession, continuation, this);

			serverSession.setScheduler(scheduler);
			request.setAttribute(SCHEDULER_ATTRIBUTE, scheduler);
			request.setAttribute(REQUEST_TICK_ATTRIBUTE, requestStartTick);
			continuation.suspend(response);
		}
	}


	private long computeTimeout() {
		long baseTimeout = transport.getTimeout();

		long delta = (System.nanoTime() - requestStartTick) / 1000000;
		if (delta > baseTimeout)
			return 0;

		if (delta < 0)
			return baseTimeout;

		return baseTimeout - delta;
	}


	private void sendQueuedMessages() {
		if (serverSession != null) {
			final List<ServerMessage> queue = serverSession.takeQueue();
			for (ServerMessage message : queue) {
				sendReply(message);
			}
		}
	}


	private boolean isMetaConnectDeliveryOnly() {
		return transport.isMetaConnectDeliveryOnly() || serverSession.isMetaConnectDeliveryOnly();
	}


	private void sendReply(ServerMessage reply) {
		if (reply == null)
			return;

		Map<String, Object> adviceFields = reply.asMutable().getAdvice(true);

		adviceFields.put(Message.INTERVAL_FIELD, computeTimeout());
		if (serverSession != null)
			serverSession.reAdvise();

		writeMessage(reply.getJSON());
	}


	private void writeMessage(String message) {
		if (message == null)
			return;

		try {
			if (writer == null)
				writer = new JSONStreamPrintWriter(response.getWriter());

			writer.write(message);
			info(" >>>> ", message);

			flushResponse();
		} catch (IOException e) {
			info("Could not successfully write message: ", e);
		}
	}


	private void finishResponse() {
		if (writer == null)
			return;

		writer.close();
	}


	private void flushResponse() {
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


	private void handleHandshake(ServerMessage reply) {
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
	private ServerSessionImpl retrieveSession(String clientId) {
		return (ServerSessionImpl) getBayeux().getSession(clientId);
	}


	/**
	 * @return true if the given clientId does not match the given
	 *         serverSession.
	 */
	private boolean isClientSessionMismatch(String clientId, ServerSessionImpl serverSession) {
		return clientId != null && !clientId.equals(serverSession.getId());
	}
}
