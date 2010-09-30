
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
import org.apache.tapestry5.services.Request;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.zanoccio.axirassa.util.HibernateTools;
import com.zanoccio.axirassa.webapp.annotations.PublicPage;
import com.zanoccio.axirassa.webapp.utilities.AxPlotDataPackage;
import com.zanoccio.axirassa.webapp.utilities.AxPlotDataPackage.AxPlotAxisLabelingFunction;
import com.zanoccio.axirassa.webapp.utilities.AxPlotDataSet;
import com.zanoccio.axirassa.webapp.utilities.AxPlotRange;

@PublicPage
@Import(library = "${tapestry.scriptaculous}/prototype.js")
public class Sentinel {

	private final static String cpusql = "SELECT cpu, date, user, system FROM SentinelCPUStats WHERE Machine_ID = 1 ORDER BY cpu ASC, date ASC";
	private final static String memsql = "SELECT date, used, total FROM SentinelMemoryStats WHERE Machine_ID = 1 ORDER BY date ASC";
	private final static String disksql = "SELECT disk, date, used, total FROM SentinelDiskUsageStats WHERE Machine_ID = 1 ORDER BY disk ASC, date ASC";

	@Inject
	private Request request;


	public Object onActionFromCpuupdate() {
		// this codes makes certain assumptions about the ordering from the
		// database query; in particular that data is grouped by CPU and then by
		// date.

		if (!request.isXHR())
			// cleanly handle non-JS
			return "Sentinel";

		Session session = HibernateTools.getSession();

		// execute the search query
		// session.beginTransaction();
		SQLQuery query = session.createSQLQuery(cpusql);
		List<Object[]> data = query.list();
		session.close();

		ArrayList<List<Object[]>> cpudata = new ArrayList<List<Object[]>>();

		// split data into sublists per CPU
		int currentcpu = -1;
		int laststart = 0;
		int index = 0;

		for (Object[] row : data) {
			int cpuid = (Integer) row[0];

			if (cpuid != currentcpu) {
				if (currentcpu > -1)
					// store the sublist
					cpudata.add(data.subList(laststart, index));

				currentcpu = cpuid;
				laststart = index;
			}

			index++;
		}

		cpudata.add(data.subList(laststart, index));

		// start building a data packet
		AxPlotDataPackage datapackage = new AxPlotDataPackage();

		AxPlotRange yrange = new AxPlotRange(0, 100);
		int cpuindex = 0;
		for (List<Object[]> dataset : cpudata) {
			index = 0;

			double[] times = new double[dataset.size()];

			// for storing <user,system> tuples:
			double[][] rawdata = new double[dataset.size()][2];

			for (Object[] row : dataset) {
				Timestamp time = (Timestamp) row[1];
				long realtime = time.getTime();
				double user = 100 * ((BigDecimal) row[2]).doubleValue();
				double system = 100 * ((BigDecimal) row[3]).doubleValue();

				times[index] = realtime;
				rawdata[index][0] = user;
				rawdata[index][1] = system;

				index++;
			}

			AxPlotDataSet currentdataset = new AxPlotDataSet();
			currentdataset.setLabel("CPU " + cpuindex);
			currentdataset.setData(times, rawdata);
			currentdataset.setYRange(yrange);

			datapackage.addDataSet(currentdataset);

			cpuindex++;
		}

		datapackage.setAggregatedYAxisRange(new AxPlotRange(0, cpuindex * 100));
		datapackage.setYAxisLabelingFunction(AxPlotAxisLabelingFunction.PERCENT);
		datapackage.setLabelDataSets(false);

		return datapackage.toJSON();
	}


	public Object onActionFromMemupdate() {
		if (!request.isXHR())
			return "Sentinel";

		Session session = HibernateTools.getSession();

		// session.beginTransaction();
		SQLQuery query = session.createSQLQuery(memsql);
		List<Object[]> result = query.list();
		session.close();

		double[] timestamps = new double[result.size()];
		double[] dataset = new double[result.size()];

		long maxmemory = 0;
		int i = 0;
		for (Object[] row : result) {
			Timestamp time = (Timestamp) row[0];
			long used = Long.parseLong((String) row[1]);
			long total = Long.parseLong((String) row[2]);

			if (total > maxmemory)
				maxmemory = total;

			timestamps[i] = time.getTime();
			dataset[i] = used;

			i++;
		}

		AxPlotDataSet memdata = new AxPlotDataSet();
		memdata.setData(timestamps, dataset);
		memdata.setLabel("Memory");

		AxPlotDataPackage plotdata = new AxPlotDataPackage();
		plotdata.addDataSet(memdata);
		plotdata.setAggregatedYAxisRange(new AxPlotRange(0, maxmemory));
		plotdata.setYAxisLabelingFunction(AxPlotAxisLabelingFunction.DATA);

		return plotdata.toJSON();
	}


	public Object onActionFromDiskSpaceUpdate() {
		Session session = HibernateTools.getSession();

		// execute the search query
		// session.beginTransaction();
		SQLQuery query = session.createSQLQuery(disksql);
		List<Object[]> data = query.list();
		session.close();

		HashMap<String, Integer> diskindices = new HashMap<String, Integer>();
		HashMap<Integer, Integer> datapoints = new HashMap<Integer, Integer>();

		// compute the number of disks and accumulate all timestamps for which
		// we have data
		int diskcount = 0;
		String currentdisk = null;

		ArrayList<String> labels = new ArrayList<String>(diskcount);

		TreeSet<Long> times = new TreeSet<Long>();
		int datapointcount = 0;
		for (Object[] row : data) {
			String disk = (String) row[0];
			Timestamp time = (Timestamp) row[1];
			times.add(time.getTime());
			datapointcount++;

			if (!disk.equals(currentdisk)) {
				currentdisk = disk;
				labels.add(disk);

				// store the disk -> index mapping
				diskindices.put(currentdisk, diskcount);
				diskcount++;
			}
		}

		// fill out the rawdata buffer
		currentdisk = null;
		int diskindex = 0;
		Iterator<Long> timeiter = null;
		int timeindex = 0;
		long maxmemory = 0;
		long totalmemory = 0;

		// a buffer with a position for each <time, cpu> slot.
		double[] timestamps = new double[times.size()];
		double[] rawdata = new double[times.size()];

		AxPlotDataPackage plotdata = new AxPlotDataPackage();
		AxPlotDataSet dataset = new AxPlotDataSet();
		for (Object[] row : data) {
			// pull data
			String disk = (String) row[0];
			Timestamp time = (Timestamp) row[1];
			long realtime = time.getTime();
			long used = Long.parseLong((String) row[2]);
			long total = Long.parseLong((String) row[3]);

			if (total > maxmemory)
				maxmemory = total;

			// are we at the next cpu?
			if (!disk.equals(currentdisk)) {
				totalmemory += maxmemory;
				maxmemory = 0;
				currentdisk = disk;
				diskindex = diskindices.get(currentdisk);

				timeiter = times.iterator();
				timeindex = 0;
			}

			rawdata[timeindex] = used;
			timeindex++;
		}

		plotdata.setLabelDataSets(true);
		plotdata.setAggregatedYAxisRange(new AxPlotRange(0, totalmemory));
		plotdata.setYAxisLabelingFunction(AxPlotAxisLabelingFunction.DATA);
		return plotdata.toJSON();
	}
}