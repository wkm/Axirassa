
package axirassa.services;

import org.hibernate.Session;

import axirassa.model.PasswordResetTokenEntity;
import axirassa.util.HibernateTools;
import axirassa.util.MessagingTools;

public class DatabaseCleanerService implements Service {

	private final Session session;


	public DatabaseCleanerService(Session session) {
		this.session = session;
	}


	@Override
	public void execute() throws Exception {
		removeExpiredTokens();
		aggregateAndSendFeedback();
	}


	private void aggregateAndSendFeedback() throws Exception {
		FeedbackAggregationService service = new FeedbackAggregationService(session,
		        MessagingTools.getEmbeddedSession());
		service.execute();
	}


	private void removeExpiredTokens() {
		session.beginTransaction();

		int removed = PasswordResetTokenEntity.removeExpiredTokens(session);
		System.out.println("Removed " + removed + " expired password tokens");

		// removed = PhoneNumberTokenEntity.removeExpiredTokens(session);
		// System.out.println("Removed " + removed +
		// " expired phone number tokens");

		session.getTransaction().commit();
	}


	public static void main(String[] args) throws Exception {
		new DatabaseCleanerService(HibernateTools.getLightweightSession()).execute();
	}
}
