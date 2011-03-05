
package axirassa.services;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import axirassa.model.UserEntity;
import axirassa.util.HibernateTools;

public class DatabaseValidationService {
	public static void main(String[] args) {
		Session session = HibernateTools.getLightweightSession();
		Query query = session.createQuery("from UserEntity");
		List<UserEntity> users = query.list();

		System.out.println("VALIDATED");
		System.out.println("Users:");
		for (UserEntity user : users)
			System.out.println(user.getEmail());

		return;
	}
}
