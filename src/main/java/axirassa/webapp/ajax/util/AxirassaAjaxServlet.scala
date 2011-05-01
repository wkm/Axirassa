
package axirassa.webapp.ajax.util;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.cometd.bayeux.Message;
import org.cometd.bayeux.server.BayeuxServer;
import org.cometd.bayeux.server.ServerMessage;
import org.cometd.bayeux.server.ServerSession;
import org.cometd.server.AbstractService;
import org.cometd.server.BayeuxServerImpl;
import org.cometd.server.CometdServlet;

import axirassa.webapp.ajax.PingerStreamingService;
import axirassa.webapp.ajax.TimeService;
import axirassa.webapp.ajax.httpstream.HttpStreamingTransport;

class AxirassaAjaxServlet extends CometdServlet {
  override
  def init() {
    super.init();

    new Monitor(getBayeux());
    try {
      new TimeService(getBayeux());
      new PingerStreamingService(getBayeux());
    } catch {
      case e : InterruptedException =>
        e.printStackTrace();
    }

    System.err.println(getBayeux().dump());
    System.err.println(getBayeux().getCurrentTransport());
  }

  override
  protected def newBayeuxServer() = {
    val server = new BayeuxServerImpl();

    server.addTransport(new HttpStreamingTransport(server, HttpStreamingTransport.PREFIX));

    val allowedTransports = new ArrayList[String](server.getAllowedTransports());
    allowedTransports.add(0, HttpStreamingTransport.PREFIX);
    server.setAllowedTransports(allowedTransports);

    server;
  }

  override
  protected def service(request : HttpServletRequest, response : HttpServletResponse) {
    try {
      super.service(request, response);
    } catch {
      case e : IllegalStateException =>
        System.err.println("IGNORING EXCEPTION:");
        e.printStackTrace();
    }
  }

  class Monitor(server : BayeuxServer) extends AbstractService(server, "monitor") {
    addService("/meta/subscribe", "monitorSubscribe");
    addService("/meta/unsubscribe", "monitorUnsubscribe");
    addService("/meta/*", "monitorMeta");

    addService("/*", "monitorAll");

    def monitorSubscribe(session : ServerSession, message : ServerMessage) {
      System.out.println("AXMONITOR Subscribe from "+session+" for "+message.get(Message.SUBSCRIPTION_FIELD));
    }

    def monitorUnsubscribe(session : ServerSession, message : ServerMessage) {
      System.out.println("AXMONITOR Unsubscribe from "+session+" for "+message.get(Message.SUBSCRIPTION_FIELD));
    }

    def monitorMeta(session : ServerSession, message : ServerMessage) {
      // if (Log.isDebugEnabled())
      System.out.println("AXMONITOR META: "+message.toString());
      System.out.println("AXMONITOR DATA: "+message.getDataAsMap());
    }

    def monitorAll(session : ServerSession, message : ServerMessage) {
      System.out.println("AXMONITOR BLAST: "+message.toString());
    }
  }
}
