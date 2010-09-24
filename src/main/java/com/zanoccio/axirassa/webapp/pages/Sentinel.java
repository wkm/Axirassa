
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

	private final static String cpusql = "SELECT date, SUM(user), SUM(system) FROM SentinelCPUStats WHERE Machine_ID = 1 GROUP BY date ORDER BY date DESC";
	private final static String memsql = "SELECT date, used, total FROM SentinelMemoryStats WHERE Machine_ID = 1 ORDER BY date DESC";

	// @InjectComponent
	// private Request request;

	@Inject
	private Request request;

	@Inject
	private ComponentResources resources;


	Object onActionFromCpuupdate() {
		if (!request.isXHR())
			// cleanly handle non-JS
			return "Sentinel";

		Session session = HibernateTools.getSession();

		// execute a search query
		Transaction transaction = session.beginTransaction();
		SQLQuery query = session.createSQLQuery(cpusql);
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


	Object onActionFromMemupdate() {
		if (!request.isXHR())
			return "Sentinel";

		Session session = HibernateTools.getSession();

		Transaction transaction = session.beginTransaction();
		SQLQuery query = session.createSQLQuery(memsql);
		List<Object[]> result = query.list();

		JSONArray finalresult = new JSONArray();
		JSONArray data = new JSONArray();

		long maxmemory = 0;
		for (Object[] row : result) {
			Timestamp time = (Timestamp) row[0];
			long used = Long.parseLong((String) row[1]);
			long total = Long.parseLong((String) row[2]);

			if (total > maxmemory)
				maxmemory = total;

			data.put(new JSONArray(time.getTime(), used));
		}

		finalresult.put(maxmemory);
		finalresult.put(data);

		session.close();

		return finalresult;
	}
}
