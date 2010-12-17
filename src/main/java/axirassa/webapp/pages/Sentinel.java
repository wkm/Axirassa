
package axirassa.webapp.pages;

import java.sql.Timestamp;
import java.util.List;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.hibernate.Query;
import org.hibernate.Session;

import zanoccio.javakit.ListUtilities;
import axirassa.webapp.annotations.PublicPage;
import axirassa.webapp.utilities.AxPlotDataPackage;
import axirassa.webapp.utilities.AxPlotDataPackage.AxPlotAxisLabelingFunction;
import axirassa.webapp.utilities.AxPlotDataSet;
import axirassa.webapp.utilities.AxPlotRange;

@PublicPage
@Import(library = "${tapestry.scriptaculous}/prototype.js")
public class Sentinel {
	@Inject
	private Request request;

	@Inject
	private Session session;


	/**
	 * axir/sentinel/cpuupdate/24h 7d 30d 365d
	 * 
	 * @return
	 */
	public Object onActionFromCpuupdate() {
		// this codes makes certain assumptions about the ordering from the
		// database query; in particular that data is grouped by CPU and then by
		// date.

		if (request != null && !request.isXHR())
			// cleanly handle non-JS
			return "Sentinel";

		// execute the search query
		Query query = session.getNamedQuery("sentinel.cpu");
		List<Object[]> data = query.list();

		List<List<Object[]>> cpudata = ListUtilities.partition(data, new ListUtilities.ListPartitioner<Object[]>() {
			@Override
			public boolean partition(Object[] previous, Object[] current) {
				return (!previous[0].equals(current[0]));
			}
		});

		// build the individual datasets
		AxPlotDataPackage datapackage = new AxPlotDataPackage();

		AxPlotRange yrange = new AxPlotRange(0, 100);
		int cpuindex = 0;
		int index = 0;
		for (List<Object[]> dataset : cpudata) {
			index = 0;

			double[] times = new double[dataset.size()];

			// for storing <user,system> tuples:
			double[][] rawdata = new double[dataset.size()][2];

			for (Object[] row : dataset) {
				Timestamp time = (Timestamp) row[1];
				long realtime = time.getTime();
				double user = 100 * ((Double) row[2]);
				double system = 100 * ((Double) row[3]);

				times[index] = realtime;
				rawdata[index][0] = user;
				rawdata[index][1] = system;

				index++;
			}

			AxPlotDataSet currentdataset = new AxPlotDataSet();
			currentdataset.setLabel("CPU " + cpuindex);
			currentdataset.setData(times, rawdata);
			currentdataset.setYPlotRange(yrange);

			datapackage.addDataSet(currentdataset);

			cpuindex++;
		}

		datapackage.setAggregatedYAxisRange(new AxPlotRange(0, cpuindex * 100));
		datapackage.setYAxisLabelingFunction(AxPlotAxisLabelingFunction.PERCENT);
		datapackage.setLabelDataSets(false);

		datapackage.setDataFormat("%2.1f");

		return datapackage.toJSON();
	}


	public Object onActionFromMemupdate() {
		if (!request.isXHR())
			return "Sentinel";

		Query query = session.getNamedQuery("sentinel.memory");
		List<Object[]> result = query.list();

		double[] timestamps = new double[result.size()];
		double[] dataset = new double[result.size()];

		long maxmemory = 0;
		int i = 0;
		for (Object[] row : result) {
			Timestamp time = (Timestamp) row[0];
			long used = ((Long) row[1]).longValue();
			long total = ((Long) row[2]).longValue();

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
		if (!request.isXHR())
			return "Sentinel";

		// execute the search query
		// session.beginTransaction();
		Query query = session.getNamedQuery("sentinel.disk");
		List<Object[]> data = query.list();

		List<List<Object[]>> diskdata = ListUtilities.partition(data, new ListUtilities.ListPartitioner<Object[]>() {
			@Override
			public boolean partition(Object[] previous, Object[] current) {
				return (!previous[0].equals(current[0]));
			}
		});

		// build per-disk datasets
		AxPlotDataPackage datapackage = new AxPlotDataPackage();
		int index = 0;
		double totalmemory = 0;
		for (List<Object[]> dataset : diskdata) {
			index = 0;
			double maxsize = 0;
			double[] times = new double[dataset.size()];
			double[] rawdata = new double[dataset.size()];

			AxPlotDataSet currentdataset = new AxPlotDataSet();

			for (Object[] row : dataset) {
				// pull data
				String disk = (String) row[0];
				Timestamp time = (Timestamp) row[1];
				long realtime = time.getTime();
				long used = ((Long) row[2]).longValue();
				long total = ((Long) row[3]).longValue();

				if (currentdataset.getLabel() == null)
					currentdataset.setLabel("Disk - " + disk);

				times[index] = realtime;
				rawdata[index] = used;

				if (maxsize < total)
					maxsize = total;

				index++;
			}

			totalmemory += maxsize;

			currentdataset.setData(times, rawdata);
			currentdataset.setYPlotRange(0.0, maxsize);

			datapackage.addDataSet(currentdataset);
		}

		datapackage.setDataFormat("%3.01f");

		datapackage.setLabelDataSets(true);
		datapackage.setAggregatedYAxisRange(new AxPlotRange(0.0, totalmemory));
		datapackage.setYAxisLabelingFunction(AxPlotAxisLabelingFunction.DATA);
		return datapackage.toJSON();
	}


	public Object onActionFromDiskIOUpdate() {
		if (!request.isXHR())
			return "Sentinel";

		Query query = session.getNamedQuery("sentinel.diskio");
		List<Object[]> data = query.list();

		List<List<Object[]>> iodata = ListUtilities.partition(data, new ListUtilities.ListPartitioner<Object[]>() {
			@Override
			public boolean partition(Object[] previous, Object[] current) {
				return (!previous[0].equals(current[0]));
			}
		});

		// build per-disk datasets
		AxPlotDataPackage datapackage = new AxPlotDataPackage();
		for (List<Object[]> dataset : iodata) {
			int index = 0;
			double[] times = new double[dataset.size()];
			double[][] rawdata = new double[dataset.size()][2];

			AxPlotDataSet currentdataset = new AxPlotDataSet();
			for (Object[] row : dataset) {
				String iface = (String) row[0];
				Timestamp time = (Timestamp) row[1];
				long realtime = time.getTime();
				double readrate = (Float) row[2];
				double writerate = (Float) row[3];

				if (currentdataset.getLabel() == null)
					currentdataset.setLabel(iface);

				times[index] = realtime;
				rawdata[index][0] = readrate;
				rawdata[index][1] = writerate;

				index++;
			}

			currentdataset.setData(times, rawdata);
			currentdataset.setYPlotRange(0.0, null);
			datapackage.addDataSet(currentdataset);
		}

		datapackage.setLabelDataSets(true);
		datapackage.setYAxisLabelingFunction(AxPlotAxisLabelingFunction.DATA);

		return datapackage.toJSON();
	}


	public Object onActionFromNetworkUpdate() {
		if (!request.isXHR())
			return "Sentinel";

		Query query = session.getNamedQuery("sentinel.net");
		List<Object[]> data = query.list();

		// partition on the network interface
		List<List<Object[]>> networkdata = ListUtilities.partition(data, new ListUtilities.ListPartitioner<Object[]>() {
			@Override
			public boolean partition(Object[] previous, Object[] current) {
				return (!previous[0].equals(current[0]));
			}
		});

		// build datasets per network interface
		AxPlotDataPackage datapackage = new AxPlotDataPackage();
		for (List<Object[]> dataset : networkdata) {
			int index = 0;
			double[] times = new double[dataset.size()];
			double[][] rawdata = new double[dataset.size()][2];

			AxPlotDataSet currentdataset = new AxPlotDataSet();

			for (Object[] row : dataset) {
				String iface = (String) row[0];
				Timestamp time = (Timestamp) row[1];
				long realtime = time.getTime();
				long sent = ((Long) row[2]).longValue();
				long received = ((Long) row[3]).longValue();

				if (currentdataset.getLabel() == null)
					currentdataset.setLabel(iface);

				times[index] = realtime;
				rawdata[index][0] = sent;
				rawdata[index][1] = received;

				index++;
			}

			currentdataset.setData(times, rawdata);
			currentdataset.setYPlotRange(0.0, null);
			datapackage.addDataSet(currentdataset);
		}

		datapackage.setLabelDataSets(true);
		datapackage.setYAxisLabelingFunction(AxPlotAxisLabelingFunction.DATA);

		return datapackage.toJSON();
	}


	public Object onActionFromNetworkIOUpdate() {
		if (!request.isXHR())
			return "Sentinel";

		Query query = session.getNamedQuery("sentinel.netio");
		List<Object[]> data = query.list();

		List<List<Object[]>> iodata = ListUtilities.partition(data, new ListUtilities.ListPartitioner<Object[]>() {
			@Override
			public boolean partition(Object[] previous, Object[] current) {
				return (!previous[0].equals(current[0]));
			}
		});

		// build per network interface
		AxPlotDataPackage datapackage = new AxPlotDataPackage();
		for (List<Object[]> dataset : iodata) {
			int index = 0;
			double[] times = new double[dataset.size()];
			double[][] rawdata = new double[dataset.size()][2];

			AxPlotDataSet currentdataset = new AxPlotDataSet();
			for (Object[] row : dataset) {
				String iface = (String) row[0];
				Timestamp time = (Timestamp) row[1];
				long realtime = time.getTime();
				double rxrate = ((Long) row[2]).longValue();
				double txrate = ((Long) row[3]).longValue();

				if (currentdataset.getLabel() == null)
					currentdataset.setLabel(iface);

				times[index] = realtime;
				rawdata[index][0] = rxrate;
				rawdata[index][1] = txrate;

				index++;
			}

			currentdataset.setData(times, rawdata);
			currentdataset.setYPlotRange(0.0, null);
			datapackage.addDataSet(currentdataset);
		}

		datapackage.setLabelDataSets(true);
		datapackage.setYAxisLabelingFunction(AxPlotAxisLabelingFunction.DATA);

		return datapackage.toJSON();
	}
}
