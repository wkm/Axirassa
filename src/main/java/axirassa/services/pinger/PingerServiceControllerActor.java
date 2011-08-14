
package axirassa.services.pinger;

import java.util.ArrayList;
import java.util.LinkedList;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.hornetq.api.core.client.ClientMessage;

import zanoccio.javakit.lambda.Function1;
import akka.actor.ActorRef;
import akka.actor.Actors;
import akka.actor.UntypedActor;
import axirassa.model.HttpStatisticsEntity;
import axirassa.util.MessageClassCallback;

@Slf4j
public class PingerServiceControllerActor extends UntypedActor {

	@Data
	public class PingRequestMessage {
		// simple message
	};





	@Data
	public class SpawnPingerMessage {
		// empty message
	};





	@Data
	public class ShutdownPingerMessage {
		// empty message
	};





	@Data
	public class PingReplyMessage {
		private ClientMessage message;
		private HttpStatisticsEntity statistic;
	}
	
	@Data
	public class PingerActorReadyMessage {
		private ActorRef pinger;
	}


	private ActorRef replyActor;

	private MessageClassCallback callbacks = new MessageClassCallback();
	private LinkedList<ActorRef> availablePingActors = new LinkedList<ActorRef>();
	private LinkedList<PingRequestMessage> pingRequestQueue = new LinkedList<PingRequestMessage>();
	private ArrayList<ActorRef> pingerActorsList = new ArrayList<ActorRef>();


	public PingerServiceControllerActor() {
		log.info("creating");
		callbacks = new MessageClassCallback();

		// PING REQUESTS
		callbacks.dispatch(PingRequestMessage.class, new Function1<Object, PingRequestMessage>() {
			@Override
			public Object call(PingRequestMessage pingRequest) throws Exception {
				log.info("ping request");

				if (availablePingActors.size() > 0) {
					log.info("pingers available; posting");
					availablePingActors.remove().sendOneWay(pingRequest);
				} else {
					log.info("no pingers available, queuing request");
					pingRequestQueue.add(pingRequest);
				}

				return null;
			}
		});

		callbacks.dispatch(PingReplyMessage.class, new Function1<Object, PingReplyMessage>() {
			@Override
			public Object call(PingReplyMessage reply) throws Exception {
				log.info("forwarding ping reply");
				getContext().forward(reply, replyActor);
				return null;
			}
		});

		callbacks.dispatch(SpawnPingerMessage.class, new Function1<Object, SpawnPingerMessage>() {
			@Override
			public Object call(SpawnPingerMessage msg) throws Exception {
				log.info("@@@@ SPAWN");

				ActorRef pingerActor = Actors.actorOf(PingActor.class);
				pingerActor.start();
				pingerActorsList.add(pingerActor);
				return null;
			}
		});
		
		callbacks.dispatch(ShutdownPingerMessage.class, new Function1<Object, ShutdownPingerMessage>() {
			@Override
            public Object call(ShutdownPingerMessage p1) throws Exception {
				log.info("SHUTDOWN ##### IGNORED");
	            return null;
            }
		});
		
		callbacks.dispatch(PingerActorReadyMessage.class, new Function1<Object, PingerActorReadyMessage>() {
			@Override
            public Object call(PingerActorReadyMessage pinger) throws Exception {
	            log.info("PINGER READY");
	            if(pingRequestQueue.size() > 0) {
	            	log.info("requested queued; posting request to pinger immediately");
	            	pinger.getPinger().sendOneWay(pingRequestQueue.remove());
	            } else {
	            	log.info("no requests available; queueing actor");
	            	availablePingActors.add(pinger.getPinger());
	            }
	            return null;
            }
		});
		
		log.info("created.");
	}


	@Override
	public void onReceive(Object message) throws Exception {
		callbacks.handle(message);
	}
}
