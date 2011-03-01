
package test.axirassa.domain;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import axirassa.model.FeedbackEntity;
import axirassa.model.UserEntity;
import axirassa.util.AbstractDomainTest;

public class TestFeedback extends AbstractDomainTest {
	@Test
	public void feedback() {
		session.beginTransaction();

		UserEntity user = new UserEntity();
		user.setEMail("who@foo.com");
		user.createPassword("password");
		session.save(user);

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

		List<FeedbackEntity> feedbacks = FeedbackEntity.getAllFeedback(session);
		assertEquals(feedbacks.size(), 2);
	}

}
