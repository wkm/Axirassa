
package axirassa.webapp.pages.monitor;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.util.EnumValueEncoder;
import org.hibernate.Session;
import org.tynamo.security.services.SecurityService;

import axirassa.model.MonitorType;
import axirassa.model.MonitorTypeEntity;
import axirassa.model.PingerFrequency;
import axirassa.model.PingerEntity;
import axirassa.model.UserEntity;

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
		// save the pinger
		PingerEntity pinger = new PingerEntity();
		pinger.setUrl(url);
		pinger.setFrequency(monitorFrequency);
		pinger.setUser(UserEntity.getUserByEmail(session, (String) security.getSubject().getPrincipal()));

		LinkedHashSet<MonitorTypeEntity> monitortypes = new LinkedHashSet<MonitorTypeEntity>();

		if (httpMonitor)
			monitortypes.add(new MonitorTypeEntity(pinger, MonitorType.HTTP));
		if (httpsMonitor)
			monitortypes.add(new MonitorTypeEntity(pinger, MonitorType.HTTPS));
		if (icmpMonitor)
			monitortypes.add(new MonitorTypeEntity(pinger, MonitorType.ICMP_PING));

		pinger.setMonitorType(monitortypes);

		session.beginTransaction();
		for (MonitorTypeEntity monitor : monitortypes)
			session.save(monitor);
		session.save(pinger);
		session.getTransaction().commit();

		return "Monitor/List";
	}
}
