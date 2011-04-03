
package test.axirassa.domain;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collections;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.junit.Test;
import org.junit.runner.RunWith;

import axirassa.ioc.IocIntegrationTestRunner;
import axirassa.model.MonitorType;
import axirassa.model.MonitorTypeEntity;
import axirassa.model.PingerEntity;
import axirassa.model.PingerFrequency;
import axirassa.model.UserEntity;
import axirassa.model.flows.CreateUserFlow;

@RunWith(IocIntegrationTestRunner.class)
public class TestPingerEntity {

	@Inject
	private CreateUserFlow createUserFlow;

	@Inject
	private Session database;


	@Test
	public void testPingerSize () throws IOException {
		createUserFlow.setEmail("foo@mail.com");
		createUserFlow.setPassword("password");
		createUserFlow.execute();

		UserEntity user = createUserFlow.getUserEntity();

		MonitorTypeEntity type = new MonitorTypeEntity();
		type.setType(MonitorType.HTTP);

		PingerEntity pinger = new PingerEntity();
		pinger.setFrequency(PingerFrequency.MINUTE);
		pinger.setMonitorType(Collections.singleton(type));
		pinger.setUrl("www.google.com");
		pinger.setUser(user);

		database.beginTransaction();
		database.save(user);
		// create many of these pingers
		for (int i = 0; i < 100; i++)
			database.save(pinger);
		database.save(type);
		database.getTransaction().commit();

		assertTrue(pinger.toBytes().length < 1500);
	}

}
