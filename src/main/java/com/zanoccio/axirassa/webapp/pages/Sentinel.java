
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

		// compute the number of CPUs and the number of data points per CPU
		int cpucount = 0;
		int currentcpu = -1;
		int dataindex = 0;
		ArrayList<Integer> datapoints = new ArrayList<Integer>();

		for (Object[] row : data) {
			int cpuid = (Integer) row[0];

			if (cpuid != currentcpu) {
				if (currentcpu > -1)
					// store the number of datapoints for this CPU
					datapoints.add(dataindex);

				currentcpu = cpuid;
				cpucount++;
				dataindex = 0;
			}

			dataindex++;
		}

		// start building a data packet
		AxPlotDataPackage datapackage = new AxPlotDataPackage();
		AxPlotDataSet currentdataset = null;

		currentcpu = -1;
		int cpuindex = 0;
		double[][] datablock = null;
		double[] axisblock = null;
		int datacursor = -1;

		for (Object[] row : data) {
			// pull data
			int cpuid = (Integer) row[0];
			Timestamp time = (Timestamp) row[1];
			long realtime = time.getTime();
			double user = 100 * ((BigDecimal) row[2]).doubleValue();
			double system = 100 * ((BigDecimal) row[3]).doubleValue();

			// are we at the next cpu?
			if (cpuid != currentcpu) {
				if (currentcpu > -1) {
					currentdataset.setLabel("CPU " + cpuid);
					currentdataset.setData(axisblock, datablock);
					datapackage.addDataSet(currentdataset);
				}

				datacursor = -1;
				datablock = new double[datapoints.get(cpuindex)][2];
				axisblock = new double[datapoints.get(cpuindex)];

				cpuindex++;
			}

			datacursor++;

			datablock[datacursor][0] = user;
			datablock[datacursor][1] = system;

			axisblock[datacursor] = time.getTime();
		}

		AxPlotDataPackage plotdata = new AxPlotDataPackage();
		plotdata.setAggregatedYAxisRange(new AxPlotRange(0, cpucount * 100));
		plotdata.setYAxisLabelingFunction(AxPlotAxisLabelingFunction.PERCENT);
		return plotdata.toJSON();
	}


	public Object onActionFromMemupdate() {
		if (!request.isXHR())
			return "Sentinel";

		Session session = HibernateTools.getSession();

		// session.beginTransaction();
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

		AxPlotDataPackage plotdata = new AxPlotDataPackage();
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

		// compute the number of disks and accumulate all timestamps for which
		// we have data
		int diskcount = 0;
		String currentdisk = null;

		ArrayList<String> labels = new ArrayList<String>(diskcount);

		TreeSet<Long> times = new TreeSet<Long>();
		for (Object[] row : data) {
			String disk = (String) row[0];
			Timestamp time = (Timestamp) row[1];
			times.add(time.getTime());

			if (!disk.equals(currentdisk)) {
				currentdisk = disk;
				labels.add(disk);

				// store the disk -> index mapping
				diskindices.put(currentdisk, diskcount);
				diskcount++;
			}
		}

		// a buffer with a position for each <time, cpu> slot.
		Double[][][] rawdata = new Double[diskcount][times.size()][1];

		// fill out the rawdata buffer
		currentdisk = null;
		int diskindex = 0;
		Iterator<Long> timeiter = null;
		int timeindex = 0;
		long maxmemory = 0;
		long totalmemory = 0;

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

			// skip over any missing times
			while (timeiter.next() < realtime) {
				rawdata[diskindex][timeindex][0] = null;
				timeindex++;
			}

			rawdata[diskindex][timeindex][0] = (double) used;
			timeindex++;
		}

		AxPlotDataPackage plotdata = new AxPlotDataPackage();
		plotdata.setAggregatedYAxisRange(new AxPlotRange(0, totalmemory));
		plotdata.setYAxisLabelingFunction(AxPlotAxisLabelingFunction.DATA);
		return plotdata.toJSON();
	}
}