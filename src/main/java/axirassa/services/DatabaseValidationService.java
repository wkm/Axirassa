
package axirassa.services;

import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.metadata.ClassMetadata;

import axirassa.util.HibernateTools;

public class DatabaseValidationService {
	public static void main(String[] args) {
		Map<String, ClassMetadata> classes = HibernateTools.getSessionFactory().getAllClassMetadata();

		Session session = HibernateTools.getLightweightSession();

		System.out.println("Entity types: ");
		for (ClassMetadata classmeta : classes.values())
			System.out.println("\t" + classmeta.getEntityName());

		System.out.println("Deep-validating each type: ");
		for (ClassMetadata classmeta : classes.values()) {
			System.out.println("\t" + classmeta.getEntityName());
			Query query = session.createQuery("from " + classmeta.getEntityName());
			query.setMaxResults(0);
			query.list();
			System.out.println("\t\tVALID");
		}

		return;
	}
}
