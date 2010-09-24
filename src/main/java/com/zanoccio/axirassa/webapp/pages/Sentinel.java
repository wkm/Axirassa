
package com.zanoccio.axirassa.webapp.pages;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.services.Request;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.zanoccio.axirassa.util.HibernateTools;
import com.zanoccio.axirassa.webapp.annotations.PublicPage;

@PublicPage
@Import(library = "${tapestry.scriptaculous}/prototype.js")
public class Sentinel {

	private final static String querysql = "SELECT date, SUM(user), SUM(system) FROM SentinelCPUStats WHERE Machine_ID = 1 GROUP BY date ORDER BY date DESC";

	// @InjectComponent
	// private Request request;

	@Inject
	private Request request;

	@Inject
	private ComponentResources resources;


	@SuppressWarnings("boxing")
	Object onActionFromCpuupdate() {
		if (!request.isXHR())
			// cleanly handle non-JS
			return "Sentinel";

		Session session = HibernateTools.getSession();

		// execute a search query
		Transaction transaction = session.beginTransaction();
		SQLQuery query = session.createSQLQuery(querysql);
		List<Object[]> result = query.list();

		JSONArray obj = new JSONArray();
		for (Object[] row : result) {
			Timestamp time = (Timestamp) row[0];
			double user = 100 * ((BigDecimal) row[1]).doubleValue();
			double system = 100 * ((BigDecimal) row[2]).doubleValue();

			System.out.println("Current time: " + time);

			obj.put(new JSONArray(time.getTime(), user, system));
		}

		session.close();

		return obj;
	}
}
