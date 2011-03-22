
package axirassa.dao;

import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Query;
import org.hibernate.Session;

import axirassa.model.FeedbackEntity;

public class FeedbackDAOImpl implements FeedbackDAO {
	@Inject
	private Session database;


	@Override
	public List<FeedbackEntity> getAllFeedback () {
		Query query = database.getNamedQuery("unposted_feedback_with_users");
		return query.list();
	}
}
