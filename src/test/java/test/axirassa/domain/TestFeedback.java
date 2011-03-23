
package test.axirassa.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import axirassa.ioc.IocTestRunner;
import axirassa.model.FeedbackEntity;
import axirassa.model.UserEmailAddressEntity;
import axirassa.model.UserEntity;
import axirassa.util.AbstractDomainTest;

@RunWith(IocTestRunner.class)
public class TestFeedback extends AbstractDomainTest {

	@Test
	public void simpletest () {
		assertEquals("one", "one");
		assertEquals("two", "two");
	}


	@Test
	public void feedback () {
		session.beginTransaction();

		UserEntity user = new UserEntity();
		user.createPassword("password");
		session.save(user);

		UserEmailAddressEntity email = new UserEmailAddressEntity();
		email.setEmail("who@foo.com");
		email.setUser(user);
		email.setPrimaryEmail(true);
		session.save(email);

		FeedbackEntity feedback1 = new FeedbackEntity();
		feedback1.setComment("hi this is a great site");
		feedback1.setUseragent("axirassa-pinger");
		session.save(feedback1);

		FeedbackEntity feedback2 = new FeedbackEntity();
		feedback2.setUser(user);
		feedback2.setComment("hi this site sucks");
		feedback2.setUseragent("troll-browser");
		session.save(feedback2);

		session.getTransaction().commit();

		// List<FeedbackEntity> feedbacks = feedbackDAO.getAllFeedback();
		// assertEquals(feedbacks.size(), 2);
	}

}
