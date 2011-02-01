
package axirassa.webapp.pages.monitor;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;

import axirassa.model.HttpStatisticsEntity;
import axirassa.model.PingerEntity;

@RequiresUser
public class StatisticsMonitor {
	@Inject
	private Session session;

	@Property
	private List<HttpStatisticsEntity> statistics;

	@Property
	private HttpStatisticsEntity statistic;

	@Property
	private PingerEntity pinger;


	public void onActivate(Long id) {
		pinger = PingerEntity.findPingerById(session, id);
		statistics = PingerEntity.findStatistics(session, pinger);
	}
}
