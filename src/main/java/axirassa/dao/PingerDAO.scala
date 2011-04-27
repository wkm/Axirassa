package axirassa.dao

import java.util.Calendar
import org.hibernate.Session
import org.apache.tapestry5.ioc.annotations.Inject
import axirassa.model.HttpStatisticsEntity
import axirassa.model.PingerEntity

trait PingerDAO {
    def findPingerById(id : Long) : Option[PingerEntity]
    def findStatistics(pinger : PingerEntity) : List[HttpStatisticsEntity]
    def getDataPoints(pinger : PingerEntity, minutes : Int) : List[HttpStatisticsEntity]
}

class PingerDAOImpl extends PingerDAO {
    @Inject
    var database : Session = _

    override def findPingerById(id : Long) = {
        val query = database.getNamedQuery("pinger_and_user_by_id")
        query.setLong("id", id)

        val pingers = query.list
        if (pingers.size < 1)
            None
        else
            Some(pingers.get(0).asInstanceOf[PingerEntity])
    }

    override def findStatistics(pinger : PingerEntity) = {
        val query = database.getNamedQuery("pinger_data_pints")
        query.setEntity("pinger", pinger)

        query.list.asInstanceOf[List[HttpStatisticsEntity]]
    }

    override def getDataPoints(pinger : PingerEntity, minutes : Int) = {
        val cal = Calendar.getInstance
        cal.add(Calendar.MINUTE, -minutes)

        val query = database.getNamedQuery("pinger_data_points")
        query.setEntity("pinger", pinger)
        query.setTimestamp("timestamp", cal.getTime)

        query.list.asInstanceOf[List[HttpStatisticsEntity]]
    }
}