
package axirassa.dao;

import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Query;
import org.hibernate.Session;

import axirassa.model.HttpStatisticsEntity;
import axirassa.model.PingerEntity;

public class PingerDAOImpl implements PingerDAO {
	@Inject
	private Session database;


	/* (non-Javadoc)
     * @see axirassa.dao.PingerDAO#findPingerById(long)
     */
	@Override
    public PingerEntity findPingerById(long id) {
		Query query = database.getNamedQuery("pinger_and_user_by_id");
		query.setLong("id", id);

		List<PingerEntity> pingers = query.list();
		if (pingers.size() < 1)
			return null;

		return pingers.iterator().next();
	}


	/* (non-Javadoc)
     * @see axirassa.dao.PingerDAO#findStatistics(axirassa.model.PingerEntity)
     */
	@Override
    public List<HttpStatisticsEntity> findStatistics(PingerEntity pinger) {
		Query query = database.getNamedQuery("pinger_statistics");
		query.setEntity("pinger", pinger);

		return query.list();
	}
}
