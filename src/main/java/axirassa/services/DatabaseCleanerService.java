
package axirassa.services;

import org.hibernate.Session;

import axirassa.model.PasswordResetTokenEntity;

public class DatabaseCleanerService implements Service {

	private final Session session;


	public DatabaseCleanerService(Session session) {
		this.session = session;
	}


	@Override
	public void execute() throws Exception {
		removeExpiredTokens();
	}


	private void removeExpiredTokens() {
		session.beginTransaction();
		System.out.println("Removing expired tokens");
		int removed = PasswordResetTokenEntity.removeExpiredTokens(session);
		System.out.println("Removed " + removed + " expired password tokens");
		session.getTransaction().commit();
	}
}
