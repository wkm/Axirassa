
package axirassa.services.sentinel;

import java.util.Date;

import org.hibernate.Session;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;

public interface SentinelAgent {

	public String agentName();


	public void setMachineID(int id);


	public int getMachineID();


	public void setSigar(Sigar sigar);


	public Sigar getSigar();


	public void setDate(Date date);


	public Date getDate();


	public void execute() throws SigarException;


	public void save(Session session);

}
