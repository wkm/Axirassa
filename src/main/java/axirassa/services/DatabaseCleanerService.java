
package axirassa.services;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;

import axirassa.dao.PasswordResetTokenDAO;
import axirassa.util.MessagingTools;
import axirassa.util.TapestryTools;

public class DatabaseCleanerService implements Service {

	@Inject
	private Session database;

	@Inject
	private PasswordResetTokenDAO passwordTokens;


	@Override
	public void execute() throws Exception {
		removeExpiredTokens();
		aggregateAndSendFeedback();
	}


	private void aggregateAndSendFeedback() throws Exception {
		FeedbackAggregationService service = new FeedbackAggregationService(database,
		        MessagingTools.getEmbeddedSession());
		service.execute();
	}


	private void removeExpiredTokens() {
		database.beginTransaction();

		int removed = passwordTokens.removeExpiredTokens();
		System.out.println("Removed " + removed + " expired password tokens");

		database.getTransaction().commit();
	}


	public static void main(String[] args) throws Exception {
		DatabaseCleanerService service = TapestryTools.autobuild(DatabaseCleanerService.class);
	}
}
