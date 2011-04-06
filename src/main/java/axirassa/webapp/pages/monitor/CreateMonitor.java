
package axirassa.webapp.pages.monitor;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.util.EnumValueEncoder;
import org.hibernate.Session;

import axirassa.dao.UserDAO;
import axirassa.model.MonitorType;
import axirassa.model.MonitorTypeEntity;
import axirassa.model.PingerEntity;
import axirassa.model.PingerFrequency;
import axirassa.webapp.services.AxirassaSecurityService;
import axirassa.webapp.services.exceptions.AxirassaSecurityException;

@RequiresUser
public class CreateMonitor {
	@Inject
	private Session session;

	@Inject
	private AxirassaSecurityService security;

	@Inject
	private UserDAO userDAO;

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


	@CommitAfter
	public Object onSuccess() throws AxirassaSecurityException {
		// save the pinger
		PingerEntity pinger = new PingerEntity();
		pinger.setUrl(url);
		pinger.setFrequency(monitorFrequency);
		pinger.setUser(security.getUserEntity());

		LinkedHashSet<MonitorTypeEntity> monitortypes = new LinkedHashSet<MonitorTypeEntity>();

		if (httpMonitor)
			monitortypes.add(new MonitorTypeEntity(MonitorType.HTTP));
		if (icmpMonitor)
			monitortypes.add(new MonitorTypeEntity(MonitorType.ICMP_PING));

		pinger.setMonitorType(monitortypes);

		for (MonitorTypeEntity monitor : monitortypes)
			session.save(monitor);
		session.save(pinger);

		return ListMonitor.class;
	}
}
