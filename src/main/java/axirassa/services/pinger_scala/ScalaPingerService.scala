package axirassa.services.pinger_scala
import org.hornetq.api.core.client.ClientSession
import axirassa.config.Messaging
import org.hornetq.api.core.client.ClientConsumer
import scala.actors.Actor
import axirassa.util.MessagingTools
import axirassa.messaging.AbstractPingerThrottleMessage
import org.slf4j.LoggerFactory
import axirassa.messaging.PingerDownThrottleMessage
import axirassa.messaging.PingerUpThrottleMessage
import axirassa.model.PingerEntity
import axirassa.model.HttpStatisticsEntity
import org.hornetq.api.core.client.ClientProducer
import scala.collection.mutable.Queue
import java.util.LinkedList
import axirassa.services.pinger.HttpPinger
import org.hornetq.api.core.client.ClientMessage

class ScalaPingerService() extends axirassa.services.Service {
  def execute() = {
    val sessionFactory = MessagingTools.getSessionFactory();

    // create a session with auto-commits 
    val messaging = sessionFactory.createSession(true, true, 0);

    val requestConsumer = messaging.createConsumer(Messaging.PINGER_REQUEST_QUEUE)
    val throttlingConsumer = messaging.createConsumer(Messaging.PINGER_THROTTLING_QUEUE)
    val responseProducer = messaging.createProducer(Messaging.PINGER_RESPONSE_QUEUE)

    println("SCALA PINGER SERVICE; INITIALIZED");
    // start off the controller actor
    new ControllerActor(messaging, requestConsumer, throttlingConsumer, responseProducer).start()
    println("CONTROLLER STARTED")
  }
}

case class PingRequest(message: ClientMessage, entity: PingerEntity)
case class PingReply(message: ClientMessage, statistic: HttpStatisticsEntity)
case class PingerActorReady(actor: PingerActor)
case class SpawnPingerActor()
case class ShutdownPingerActor()

class ControllerActor(
    messaging: ClientSession,
    requestConsumer: ClientConsumer,
    throttlingConsumer: ClientConsumer,
    responseProducer: ClientProducer) extends Actor {

  val pingActors = new LinkedList[PingerActor]()
  val availablePingActors = Queue[PingerActor]()

  val pingRequestQueue = Queue[Any]()

  // the reply actor handles posting replies back onto the pinger.reply queue
  val replyActor = new ReplyActor(this, messaging, responseProducer)

  // the request actor puts requests onto the pinger.request queue
  val requestActor = new RequestActor(this, requestConsumer)

  val throttlingActor = new ThrottlingActor(this, throttlingConsumer)

  def act() {
    messaging.start()

    replyActor.start()
    requestActor.start()
    throttlingActor.start()

    loop {
      receive {
        case request: PingRequest =>
          println("CNTRL: ping request")

          if (availablePingActors.size > 0) {
            println("CNTRL: pinger available, sending")
            availablePingActors.dequeue() ! request
          } else {
            println("CNTRL: no pingers ready, queuing request")
            pingRequestQueue.enqueue(request)
          }

        case reply: PingReply =>
          println("CNTRL: ping reply")
          replyActor ! reply

        case SpawnPingerActor() =>
          println("CNTRL: @@@@ SPAWN")
          val pingerActor = new PingerActor(this)
          pingActors.add(pingerActor)
          pingerActor.start()

        case ShutdownPingerActor() =>
          println("CNTRL: shutdown")

        case PingerActorReady(actor) =>
          println("CNTRL: pinger ready")
          // if we have a ping request available, dispatch it immediately
          if (pingRequestQueue.size > 0) {
            println("CNTRL:\t\t forwarding request")
            actor ! pingRequestQueue.dequeue()
          } else {
            println("CNTRL:\t\t no requests, queuing actor")
            availablePingActors.enqueue(actor)
          }

        case _ =>
          println("CNTRL: Unknown message")
      }
    }
  }
}

class ThrottlingActor(
    controller: ControllerActor,
    throttlingConsumer: ClientConsumer) extends Actor {
  def act() {
    println("THROT: started")

    loop {
      val clientMessage = throttlingConsumer.receive();
      val castMessage = MessagingTools.fromMessageBytes(classOf[AbstractPingerThrottleMessage], clientMessage)

      castMessage match {
        case msg: PingerDownThrottleMessage =>
          println("THROT: DOWN THROTTLE")
          controller ! ShutdownPingerActor()
          clientMessage.acknowledge()

        case msg: PingerUpThrottleMessage =>
          println("THROT: UP THROTTLE")
          controller ! SpawnPingerActor()
          clientMessage.acknowledge()

        case _ =>
          println("THROT: UNKNOWN MESSAGE" + castMessage)
      }
    }
  }
}

class ReplyActor(
    controller: ControllerActor,
    messaging: ClientSession,
    responseProducer: ClientProducer) extends Actor {

  def act() {
    println("REPLY: started")

    loop {
      receive {
        case PingReply(requestMessage, statistic) =>
          println("REPLY: POSTING: " + statistic)
          val message = messaging.createMessage(true)
          message.getBodyBuffer().writeBytes(statistic.toBytes())

          responseProducer.send(message)
          responseProducer.send(statistic.getBroadcastAddress(), message)
      }
    }
  }
}

class RequestActor(
    controller: ControllerActor,
    requestConsumer: ClientConsumer) extends Actor {

  def act() {
    println("REQUEST: started")

    loop {
      val message = requestConsumer.receive()
      val entity = MessagingTools.fromMessageBytes(classOf[PingerEntity], message)

      if (entity != null) {
        message.acknowledge()
        
        println("REQUEST: received: " + entity)
        controller ! PingRequest(message, entity)
      }
    }
  }
}

class PingerActor(controller: ControllerActor) extends Actor {
  val pinger = new HttpPinger()

  def act() {
    println("PINGER: started")

    controller ! PingerActorReady(this)

    loop {
      receive {
        case PingRequest(message, entity) =>
          println("PINGER: request: " + entity)
          controller ! PingReply(message, pinger.ping(entity))
          controller ! PingerActorReady(this)

        case ShutdownPingerActor =>
          println("PINGER: !!SHUTDOWN!!")
          exit
      }
    }
  }
}























