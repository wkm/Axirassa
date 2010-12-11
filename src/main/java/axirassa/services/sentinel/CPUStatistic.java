
package axirassa.services.sentinel;

import java.util.Date;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

class CPUStatistic extends AbstractSentinelStatistic {

	private static final String CPU_STAT_INSERT = "INSERT INTO SentinelCPUStats VALUES (?,?,?,?,?)";

	private final int cpuid;
	private final double user;
	private final double system;


	public CPUStatistic(int machineid, Date timestamp, int cpuid, double user, double system) {
		super(machineid, timestamp);

		this.cpuid = cpuid;
		this.user = user;
		this.system = system;
	}


	@Override
	public void save(Session session) {
		SQLQuery query = session.createSQLQuery(CPU_STAT_INSERT);

		query.setInteger(0, getMachineID());
		query.setTimestamp(1, getTimestamp());
		query.setInteger(2, cpuid);
		query.setDouble(3, user);
		query.setDouble(4, system);

		query.executeUpdate();
	}
}
