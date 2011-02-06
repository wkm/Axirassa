
package test.axirassa.domain;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Collections;

import org.junit.Test;

import axirassa.model.MonitorType;
import axirassa.model.MonitorTypeEntity;
import axirassa.model.PingerEntity;
import axirassa.model.PingerFrequency;
import axirassa.model.UserEntity;
import axirassa.util.AbstractDomainTest;

public class TestPingerEntity extends AbstractDomainTest {
	@Test
	public void testPingerSize() throws IOException {
		UserEntity user = new UserEntity();
		user.setEMail("foo@mail.com");
		user.createPassword("password");

		MonitorTypeEntity type = new MonitorTypeEntity();
		type.setType(MonitorType.HTTP);

		PingerEntity pinger = new PingerEntity();
		pinger.setFrequency(PingerFrequency.MINUTE);
		pinger.setMonitorType(Collections.singleton(type));
		pinger.setUrl("www.google.com");
		pinger.setUser(user);

		session.beginTransaction();
		session.save(user);
		// create many of these pingers
		for (int i = 0; i < 100; i++)
			session.save(pinger);
		session.save(type);
		session.getTransaction().commit();

		assertTrue(pinger.toBytes().length < 1500);
	}
}
