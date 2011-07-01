
package test.axirassa.domain;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.junit.Test;

import axirassa.dao.FeedbackDAO;
import axirassa.model.FeedbackEntity;
import axirassa.model.UserEmailAddressEntity;
import axirassa.model.UserEntity;
import axirassa.util.test.AbstractIntegrationTest;

public class TestFeedback extends AbstractIntegrationTest {

	@Inject
	private Session database;

	@Inject
	private FeedbackDAO feedbackDAO;


	@Test
	public void feedback() throws HibernateException, SQLException {
		database.beginTransaction();

		UserEntity user = new UserEntity();
		user.createPassword("password");
		database.save(user);

		UserEmailAddressEntity email = new UserEmailAddressEntity();
		email.setEmail("who@foo.com");
		email.setUser(user);
		email.setPrimaryEmail(true);
		database.save(email);

		FeedbackEntity feedback1 = new FeedbackEntity();
		feedback1.setComment("hi this is a great site");
		feedback1.setUseragent("axirassa-pinger");
		database.save(feedback1);

		FeedbackEntity feedback2 = new FeedbackEntity();
		feedback2.setUser(user);
		feedback2.setComment("hi this site sucks");
		feedback2.setUseragent("troll-browser");
		database.save(feedback2);

		database.getTransaction().commit();

		List<FeedbackEntity> feedbacks = feedbackDAO.getAllFeedback();
		assertEquals(2, feedbacks.size());
	}

}
