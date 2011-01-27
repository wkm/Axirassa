
package axirassa.webapp.pages.monitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.util.EnumValueEncoder;
import org.hibernate.Session;
import org.tynamo.security.services.SecurityService;

import axirassa.domain.MonitorType;
import axirassa.domain.MonitorTypeModel;
import axirassa.domain.PingerFrequency;
import axirassa.domain.PingerModel;

@RequiresUser
public class CreateMonitor {
	@Inject
	private Session session;

	@Inject
	private SecurityService security;

	@Property
	private final ValueEncoder<PingerFrequency> frequencyEncoder = new EnumValueEncoder(PingerFrequency.class);

	@Property
	private String url;

	@Property
	private boolean httpMonitor;

	@Property
	private boolean httpsMonitor;

	@Property
	private boolean icmpMonitor;

	@Property
	private PingerFrequency monitorFrequency;

	@Property
	private final List<PingerFrequency> frequencies = Arrays.asList(PingerFrequency.values());


	public String onSuccess() {
		ArrayList<Object> entities = new ArrayList<Object>();

		// save the pinger
		PingerModel pinger = new PingerModel();
		pinger.setUrl(url);
		pinger.setFrequency(monitorFrequency);
		entities.add(pinger);

		if (httpMonitor)
			entities.add(new MonitorTypeModel(pinger, MonitorType.HTTP));
		if (httpsMonitor)
			entities.add(new MonitorTypeModel(pinger, MonitorType.HTTPS));
		if (icmpMonitor)
			entities.add(new MonitorTypeModel(pinger, MonitorType.ICMP_PING));

		session.beginTransaction();
		for (Object entity : entities)
			session.save(entity);
		session.getTransaction().commit();

		return "Monitor/List";
	}
}
