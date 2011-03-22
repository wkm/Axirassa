package axirassa.webapp.pages.monitor;


import axirassa.dao.PingerDAO;
import axirassa.model.HttpStatisticsEntity;
import axirassa.model.PingerEntity;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;

import java.util.List;


@RequiresUser
public class StatisticsMonitor {
	@Inject
	private Session session;

	@Inject
	private PingerDAO pingerDAO;

	@Property
	private List<HttpStatisticsEntity> statistics;

	@Property
	private HttpStatisticsEntity statistic;

	@Property
	private PingerEntity pinger;

	@Property
	private Long id;


	public void onActivate (Long id) {
		this.id = id;

		pinger = pingerDAO.findPingerById(id);
		statistics = pingerDAO.findStatistics(pinger);
	}
}
