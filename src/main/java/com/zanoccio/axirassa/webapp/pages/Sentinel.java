
package com.zanoccio.axirassa.webapp.pages;

import java.util.List;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.services.Request;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.zanoccio.axirassa.util.HibernateUtil;
import com.zanoccio.axirassa.webapp.annotations.PublicPage;

@PublicPage
@Import(library = "${tapestry.scriptaculous}/prototype.js")
public class Sentinel {

	private final static String querysql = "SELECT date, SUM(user), SUM(system) FROM SentinelCPUStats WHERE Machine_ID = 1 GROUP BY date";

	// @InjectComponent
	// private Request request;

	@Inject
	private Request request;

	@Inject
	private ComponentResources resources;


	Object onActionFromCpuupdate() {
		// if (!request.isXHR())
		// // cleanly handle non-JS
		// return "Sentinel";

		Session session = HibernateUtil.getSession();

		// execute a search query
		Transaction transaction = session.beginTransaction();
		SQLQuery query = session.createSQLQuery(querysql);
		List<Object[]> result = query.list();

		JSONArray obj = new JSONArray();
		for (Object[] row : result) {
			obj.put(row);
		}

		return obj;
	}
}
