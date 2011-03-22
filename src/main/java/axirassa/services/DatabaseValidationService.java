
package axirassa.services;

import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.type.Type;

import axirassa.util.HibernateTools;

public class DatabaseValidationService {
	public static void main (String[] args) {
		Map<String, ClassMetadata> classes = HibernateTools.getSessionFactory().getAllClassMetadata();

		Session session = HibernateTools.getLightweightSession();
		System.out.println("Deep-validating each type: ");
		for (ClassMetadata classmeta : classes.values()) {
			System.out.println("\t" + classmeta.getEntityName());

			Criteria criteria = session.createCriteria(classmeta.getEntityName());
			for (String property : classmeta.getPropertyNames()) {
				Type type = classmeta.getPropertyType(property);

				if (type.isAssociationType())
					criteria.setFetchMode(type.getName(), FetchMode.JOIN);
			}

			criteria.setMaxResults(0);
			criteria.list();

			System.out.println("\t\tVALID");
		}

		return;
	}
}
