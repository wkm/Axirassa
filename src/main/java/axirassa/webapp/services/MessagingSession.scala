
package axirassa.webapp.services

import org.hornetq.api.core.HornetQException
import org.hornetq.api.core.client.ClientMessage
import org.hornetq.api.core.client.ClientProducer
import org.hornetq.api.core.client.ClientSession

trait MessagingSession {
    def getSession : ClientSession
    def createProducer : ClientProducer
    def createProducer(queue : String) : ClientProducer
    def createMessage(durable : Boolean) : ClientMessage
    def close
}

class MessagingSessionImpl(session : ClientSession) extends MessagingSession {
    override def createProducer = session.createProducer

    override def createProducer(queue : String) = session.createProducer(queue)

    override def createMessage(durable : Boolean) = session.createMessage(durable)

    override def getSession = session

    override def close { session.close }
}
