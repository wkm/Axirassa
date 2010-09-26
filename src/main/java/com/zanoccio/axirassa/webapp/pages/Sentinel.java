
package com.zanoccio.axirassa.webapp.pages;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.zanoccio.axirassa.util.HibernateTools;
import com.zanoccio.axirassa.util.JSONConstructor;
import com.zanoccio.axirassa.webapp.annotations.PublicPage;

@PublicPage
@Import(library = "${tapestry.scriptaculous}/prototype.js")
public class Sentinel {

	private final static String cpusql = "SELECT cpu, date, user, system FROM SentinelCPUStats WHERE Machine_ID = 1 ORDER BY cpu ASC, date ASC";
	private final static String memsql = "SELECT date, used, total FROM SentinelMemoryStats WHERE Machine_ID = 1 ORDER BY date ASC";

	// @InjectComponent
	// private Request request;

	@Inject
	private Request request;

	@Inject
	private ComponentResources resources;


	Object onActionFromCpuupdate() {
		// this codes makes certain assumptions about the ordering from the
		// database query; in particular that data is grouped by CPU and then by
		// date.

		if (!request.isXHR())
			// cleanly handle non-JS
			return "Sentinel";

		Session session = HibernateTools.getSession();

		// execute the search query
		Transaction transaction = session.beginTransaction();
		SQLQuery query = session.createSQLQuery(cpusql);
		List<Object[]> data = query.list();
		session.close();

		HashMap<Integer, Integer> cpuindices = new HashMap<Integer, Integer>();

		// compute the number of CPUs and accumulate all timestamps for which we
		// have data
		int cpucount = 0;
		int currentcpu = -1;

		TreeSet<Long> times = new TreeSet<Long>();
		for (Object[] row : data) {
			int cpuid = (Integer) row[0];
			Timestamp time = (Timestamp) row[1];
			times.add(time.getTime());

			if (cpuid != currentcpu) {
				currentcpu = cpuid;
				// store the cpuid -> index mapping
				cpuindices.put(currentcpu, cpucount);
				cpucount++;
			}
		}

		// a buffer with a position for each <time, cpu> slot.
		Double[][][] rawdata = new Double[cpucount][times.size()][2];

		// fill out the rawdata buffer
		currentcpu = -1;
		int cpuindex = 0;
		Iterator<Long> timeiter = null;
		int timeindex = 0;

		for (Object[] row : data) {
			// pull data
			int cpuid = (Integer) row[0];
			Timestamp time = (Timestamp) row[1];
			long realtime = time.getTime();
			double user = 100 * ((BigDecimal) row[2]).doubleValue();
			double system = 100 * ((BigDecimal) row[3]).doubleValue();

			// are we at the next cpu?
			if (cpuid != currentcpu) {
				currentcpu = cpuid;
				cpuindex = cpuindices.get(currentcpu);

				timeiter = times.iterator();
				timeindex = 0;
			}

			// skip over any missing times
			while (timeiter.next() < realtime) {
				rawdata[cpuindex][timeindex][0] = null;
				rawdata[cpuindex][timeindex][1] = null;
				timeindex++;
			}

			rawdata[cpuindex][timeindex][0] = user;
			rawdata[cpuindex][timeindex][1] = system;
			timeindex++;
		}

		ArrayList<String> labels = new ArrayList<String>(cpucount);
		for (int i = 0; i < cpucount; i++)
			labels.add("CPU " + i);

		JSONObject result = new JSONObject();
		result.put("length", cpucount);
		result.put("labels", JSONConstructor.generateStrings(labels));
		result.put("times", JSONConstructor.generateLongs(times));
		result.put("data", JSONConstructor.generate(rawdata));
		return result;
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

class LongsDescending implements Comparator<Long> {

	@Override
	public int compare(Long arg0, Long arg1) {
		return (int) (arg1 - arg0);
	}

}
