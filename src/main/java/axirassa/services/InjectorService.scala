
package axirassa.services

import java.io.IOException
import java.util.ArrayList

import org.hibernate.Session
import org.hornetq.api.core.HornetQException
import org.hornetq.api.core.client.ClientConsumer
import org.hornetq.api.core.client.ClientMessage
import org.hornetq.api.core.client.ClientSession

import axirassa.config.Messaging
import axirassa.model.HttpStatisticsEntity
import axirassa.util.AutoSerializingObject

import scala.collection.JavaConversions._

/**
 * The InjectorService relatively frequently. It is designed to insert responses
 * on the high-speed message queue into the database in batches.
 * 
 * @author wiktor
 */

object InjectorService {
	var FLUSH_SIZE = 1000
	
	def rebuildMessage (message : ClientMessage ) = {
		if (message == null)
			null
		else {
		val buffer = new Array[Byte](message.getBodyBuffer().readableBytes())
		message.getBodyBuffer().readBytes(buffer)
		message.acknowledge()

		val rawobject = AutoSerializingObject.fromBytes(buffer)
		rawobject match {
			case e : HttpStatisticsEntity => e
			case _ => throw new InvalidMessageClassException(classOf[HttpStatisticsEntity], rawobject)
		}
		}
	}
}


class InjectorService(messagingSession : ClientSession, databaseSession : Session) extends Service {
	override
	def execute {
		val consumer = messagingSession.createConsumer(Messaging.PINGER_RESPONSE_QUEUE)
		messagingSession.start()

		val entities = new ArrayList[HttpStatisticsEntity]
		var message : ClientMessage = null
		
		do {
			message = consumer.receiveImmediate()
			if (message != null)
				entities.add(InjectorService.rebuildMessage(message))
		} while(message != null)

		consumer.close()
		messagingSession.stop()

		databaseSession.beginTransaction()
		
		var entityCounter = 0
		for (entity <- entities) {
			databaseSession.save(entity)
			entityCounter += 1

			// it's necessary to flush the session frequently to prevent the
			// memory-cache of entities to use up the heap
			if ((entityCounter % InjectorService.FLUSH_SIZE) == 0) {
				databaseSession.flush()
				databaseSession.clear()
			}
		}
		
		databaseSession.getTransaction().commit()
		databaseSession.flush()
		databaseSession.clear()
	}
}
