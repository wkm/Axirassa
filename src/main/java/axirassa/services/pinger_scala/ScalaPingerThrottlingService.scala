package axirassa.services.pinger_scala
import axirassa.services.pinger.BandwidthThreadAllocator
import axirassa.services.pinger.BandwidthMeasurer
import axirassa.services.Service
import org.hornetq.api.core.client.ClientSession
import scala.actors.Actor
import axirassa.util.MessagingTopic
import axirassa.config.Messaging
import org.hornetq.api.core.client.ClientConsumer
import axirassa.util.MessagingTools
import axirassa.model.HttpStatisticsEntity
import lombok.extern.slf4j.Slf4j

class ScalaPingerThrottlingService(
    messaging: ClientSession,
    bandwidthMeasurer: BandwidthMeasurer,
    threadAllocator: BandwidthThreadAllocator) extends Service {

  override def execute() {
    val throttlingProducer = messaging.createProducer(Messaging.PINGER_THROTTLING_QUEUE)

    val pingerResponseTopic = new MessagingTopic(messaging, "ax.account.#");
    val pingerResponseConsumer = pingerResponseTopic.createConsumer()

    val measurerActor = new BandwidthMeasurerControlActor(bandwidthMeasurer)
    measurerActor.start()

    val threadAllocatorActor = new BandwidthThreadAllocatorControlActor(threadAllocator)
    threadAllocatorActor.start()

    val bandwidthMeasurerActor = new BandwidthMeasurerActor(pingerResponseConsumer, measurerActor);
    bandwidthMeasurerActor.start()

    val threadAdjusterActor = new ThreadAdjusterActor(measurerActor, threadAllocatorActor)
    threadAdjusterActor.start()
  }
}

@Slf4j
class BandwidthMeasurerActor(
    pingerResponseConsumer: ClientConsumer,
    measurerActor: BandwidthMeasurerControlActor) extends Actor {
  
  override def act() {
    loop {
      val clientMessage = pingerResponseConsumer.receive()
      clientMessage.acknowledge()
      
      val statistic = MessagingTools.fromMessageBytes(classOf[HttpStatisticsEntity], clientMessage)
  
      
    }
  }

}

class ThreadAdjusterActor(
    measurerActor: BandwidthMeasurerControlActor,
    threadAllocatorActor: BandwidthThreadAllocatorControlActor) extends Actor {

  override def act() {
    loop {
      receive {
        case _ =>
          println("what?")
      }
    }
  }
};

case class RecordBandwidthMeasurement(rate: Long)
case class ComputeThreadDelta()
case class ThreadDelta(delta: Int)

class BandwidthThreadAllocatorControlActor(
    val allocator: BandwidthThreadAllocator) extends Actor {
  override def act() {
    loop {
      receive {
        case RecordBandwidthMeasurement(rate) =>
          allocator.addBandwidthMeasurement(rate)

        case ComputeThreadDelta() =>
          reply(ThreadDelta(allocator.getTargetThreadCount()))
      }
    }
  }
}

case class BandwidthMeasurement(
  responseSize: Long,
  responseTime: Int)

case class GetCurrentBandwidthRate(
  interval: Int)

case class CurrentBandwidthRate(
  rate: Long)

class BandwidthMeasurerControlActor(
    val measurer: BandwidthMeasurer) extends Actor {

  override def act() {
    loop {
      receive {
        case BandwidthMeasurement(responseSize, responseTime) =>
          measurer.addDownload(responseSize, System.currentTimeMillis(), responseTime)

        case GetCurrentBandwidthRate(time) =>
          reply(CurrentBandwidthRate(measurer.currentRate(time, System.currentTimeMillis)))
      }
    }
  }
}