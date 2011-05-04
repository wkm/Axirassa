
package axirassa.services;

import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;

import org.hibernate.Session;



import axirassa.config.Messaging;
import axirassa.model.PingerEntity;
import axirassa.webapp.services.MessagingSession;

import scala.collection.JavaConversions._

/**
 * The ControllerService executes every minute, creating a message for each
 * triggered pinger.
 *
 * @author wiktor
 */
class ControllerService extends Service {

    @Inject
    var messaging : MessagingSession = _

    @Inject
    var database : Session = _

    @Override
    def execute() {
        val producer = messaging.createProducer(Messaging.PINGER_REQUEST_QUEUE);

        val query = database.getNamedQuery("pingers_by_frequency");
        query.setInteger("frequency", 3600);

        val pingers = query.list.asInstanceOf[List[PingerEntity]];
        for (pinger <- pingers) {
            val message = messaging.createMessage(false);
            message.getBodyBuffer().writeBytes(pinger.toBytes);
            producer.send(message);
        }

        database.flush();
        database.clear();

        System.out.println("Populated "+pingers.size()+" pingers");

        producer.close();
    }
}
