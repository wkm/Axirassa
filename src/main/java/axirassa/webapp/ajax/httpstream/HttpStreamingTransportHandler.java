
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

public class HttpStreamingTransportHandler {

	private final static String USER_AGENT_HEADER = "User-Agent";

	private final HttpServletRequest request;
	private final HttpServletResponse response;
	private ServerSessionImpl serverSession;
	private final HttpStreamingTransport transport;
	private JSONStreamPrintWriter writer;


	public HttpStreamingTransportHandler(HttpStreamingTransport transport, HttpServletRequest request,
	        HttpServletResponse response) {
		this.transport = transport;
		this.request = request;
		this.response = response;
	}


	private void info(Object... info) {
		transport.info(info);
	}


	private BayeuxServerImpl getBayeux() {
		return transport.getBayeux();
	}


	public void handle() {
		info("handling request: ", request);
		try {
			ServerMessage.Mutable[] messages = transport.parseRequestMessages(request);
			if (messages == null) {
				info("no messages");
				return;
			}

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
			info("cleaning up HST handle");
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

			reply = getBayeux().extendReply(serverSession, serverSession, reply);
			sendReply(reply);

			if (isHandshake) {
				// immediately finish
				writer.close();
			}

			message.setAssociated(null);

			if (!isHandshake && serverSession != null) {
				// time to sleepy sleep
				info("tranport timeout: ", transport.getTimeout());
				long timeout = serverSession.calculateTimeout(transport.getTimeout());
				info("setting timeout to: ", timeout);

				if (serverSession.isConnected()) {
					info("starting timeout....");
					serverSession.startIntervalTimeout();
				}
			} else {
				info("no server session");
			}
		}

		// info("CLOSING WRITER TO FINISH SENDING");
		// finishResponse();

		return serverSession;
	}


	private void sendQueuedMessages() {
		if (serverSession != null) {
			info("SENDING QUEUED MESSAGES");
			final List<ServerMessage> queue = serverSession.takeQueue();
			for (ServerMessage message : queue) {
				info("QUEUED MESSAGE: ", message);
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
		adviceFields.put(Message.INTERVAL_FIELD, 10000);
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
			info("Wrote message: ", message);

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
			info("flushed response and HTTP buffer");
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
		info("retreiving server session associated with: " + clientId);
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
