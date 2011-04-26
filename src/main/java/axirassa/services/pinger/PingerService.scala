package axirassa.services.pinger

import org.hornetq.api.core.client.ClientSession
import org.hornetq.api.core.client.ClientProducer
import axirassa.config.Messaging
import axirassa.model.PingerEntity
import axirassa.util.AutoSerializingObject
import axirassa.services.exceptions.InvalidMessageClassException
import axirassa.model.HttpStatisticsEntity
import axirassa.services.Service

class PingerService(val messaging: ClientSession) extends Service {
	private val pinger = new HttpPinger
	
	def execute() = {
		messaging.start()
		
		val consumer = messaging.createConsumer(Messaging.PINGER_REQUEST_QUEUE)
		val producer = messaging.createProducer(Messaging.PINGER_RESPONSE_QUEUE)
		
		try {
			while(true) {
				val message = consumer.receive()
				
				var buffer = new Array[Byte](message.getBodyBuffer().readableBytes())
				message.getBodyBuffer().readBytes(buffer)
				
				var rawobject = AutoSerializingObject.fromBytes(buffer)
				rawobject match {
					case request: PingerEntity => {
						val statistic = pinger.ping(request)
						
						if(statistic != null)
							sendResponseMessages(producer, statistic)
							
						println(request.getUrl() + " TRIGGERS: " + pinger.getTriggers())
					}
					
					case _ =>
						throw new InvalidMessageClassException(classOf[PingerEntity], rawobject)					
				}
			}
		} finally {
			if(consumer != null)
				consumer.close()
				
			if(messaging != null)
				messaging.stop();
		}
	}
	
	private def sendResponseMessages(producer: ClientProducer, statistic: HttpStatisticsEntity) {
		val message = messaging.createMessage(true);
		message.getBodyBuffer().writeBytes(statistic.toBytes());
		producer.send(message);
		
		// send a message to the broadcast address 
		producer.send(getBroadcastAddress(statistic), message);
	}
	
	private def getBroadcastAddress(statistic: HttpStatisticsEntity) = {
		val pinger = statistic.getPinger()
		 
		PingerEntity.createBroadcastQueueName(pinger.getUser().getId(), pinger.getId())
	}
}
