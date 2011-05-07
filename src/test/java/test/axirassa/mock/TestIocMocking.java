
package test.axirassa.mock;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.junit.Test;

import axirassa.services.email.EmailTemplate;
import axirassa.util.test.AbstractUnitTest;
import axirassa.webapp.services.EmailNotifyService;

/**
 * 
 * @author wiktor
 */

public class TestIocMocking extends AbstractUnitTest {

	@Inject
	private Session database;

	@Inject
	private EmailNotifyService emailer;


	@Test
	public void testMocking() {
		// test
		database.beginTransaction();
		database.beginTransaction();
		emailer.startMessage(EmailTemplate.AGGREGATED_FEEDBACK);

		// verify
		verify(database, times(2)).beginTransaction();
		verify(emailer).startMessage(EmailTemplate.AGGREGATED_FEEDBACK);
	}

}
