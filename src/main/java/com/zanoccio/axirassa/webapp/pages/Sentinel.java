
package com.zanoccio.axirassa.webapp.pages;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Request;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.zanoccio.axirassa.util.HibernateTools;
import com.zanoccio.axirassa.webapp.annotations.PublicPage;
import com.zanoccio.axirassa.webapp.utilities.AxPlotData;
import com.zanoccio.axirassa.webapp.utilities.AxPlotData.AxPlotAxisLabelingFunction;

@PublicPage
@Import(library = "${tapestry.scriptaculous}/prototype.js")
public class Sentinel {

	private final static String cpusql = "SELECT cpu, date, user, system FROM SentinelCPUStats WHERE Machine_ID = 1 ORDER BY cpu ASC, date ASC";
	private final static String memsql = "SELECT date, used, total FROM SentinelMemoryStats WHERE Machine_ID = 1 ORDER BY date ASC";

	@Inject
	private Request request;


	Object onActionFromCpuupdate() {
		// this codes makes certain assumptions about the ordering from the
		// database query; in particular that data is grouped by CPU and then by
		// date.

		if (!request.isXHR())
			// cleanly handle non-JS
			return "Sentinel";

		Session session = HibernateTools.getSession();

		// execute the search query
		session.beginTransaction();
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

			rawdata[cpuindex][timeindex][0] = system;
			rawdata[cpuindex][timeindex][1] = user;
			timeindex++;
		}

		ArrayList<String> labels = new ArrayList<String>(cpucount);
		for (int i = 0; i < cpucount; i++)
			labels.add("CPU " + i);

		AxPlotData plotdata = new AxPlotData(cpucount, 2, labels, times, rawdata);
		plotdata.setAggregatedMax(cpucount * 100);
		plotdata.setYAxisLabelingFunction(AxPlotAxisLabelingFunction.PERCENT);
		return plotdata.toJSON();
	}


	Object onActionFromMemupdate() {
		if (!request.isXHR())
			return "Sentinel";

		Session session = HibernateTools.getSession();

		session.beginTransaction();
		SQLQuery query = session.createSQLQuery(memsql);
		List<Object[]> result = query.list();
		session.close();

		ArrayList<Long> timestamps = new ArrayList<Long>(result.size());
		Double[][] dataset = new Double[result.size()][1];
		ArrayList<String> labels = new ArrayList<String>(1);
		labels.add("Memory");

		long maxmemory = 0;
		int i = 0;
		for (Object[] row : result) {
			Timestamp time = (Timestamp) row[0];
			long used = Long.parseLong((String) row[1]);
			long total = Long.parseLong((String) row[2]);

			if (total > maxmemory)
				maxmemory = total;

			timestamps.add(time.getTime());
			dataset[i++][0] = (double) used;
		}

		Double[][][] rawdata = new Double[1][][];
		rawdata[0] = dataset;

		AxPlotData plotdata = new AxPlotData(1, 1, labels, timestamps, rawdata);
		plotdata.setAggregatedMax(maxmemory);
		plotdata.setYAxisLabelingFunction(AxPlotAxisLabelingFunction.DATA);

		return plotdata.toJSON();
	}


	Object onActionFromDiskSpaceUpdate() {
		return new JSONObject();
	}
}