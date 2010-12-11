
package axirassa.services.sentinel;

import java.util.Date;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

class DiskIOStatistic extends AbstractSentinelStatistic {
	private static final String DISKIO_STAT_INSERT = "INSERT INTO SentinelDiskIOStats VALUES (?,?,?,?,?)";

	private final String disk;
	private final double read;
	private final double write;


	public DiskIOStatistic(int machineid, Date timestamp, String disk, double read, double write) {
		super(machineid, timestamp);

		this.disk = disk;
		this.read = read;
		this.write = write;
	}


	@Override
	public void save(Session session) {
		SQLQuery query = session.createSQLQuery(DISKIO_STAT_INSERT);

		query.setInteger(0, getMachineID());
		query.setTimestamp(1, getTimestamp());
		query.setString(2, disk);
		query.setDouble(3, read);
		query.setDouble(4, write);

		query.executeUpdate();
	}

}
