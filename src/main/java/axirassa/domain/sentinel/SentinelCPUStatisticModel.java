
package axirassa.domain.sentinel;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "SentinelCPUStats", uniqueConstraints = { @UniqueConstraint(columnNames = {
        "`Machine_ID`", "`Date`", "`Cpu`" }) })
public class SentinelCPUStatisticModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	Long id;

	@Basic
	@Column(name = "`Machine_ID`", nullable = false)
	int machineid;

	@Basic
	@Column(name = "`Date`", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	Date date;

	@Basic
	@Column(name = "`Cpu`", nullable = false)
	int cpuid;

	@Basic
	@Column(name = "`User`", nullable = false)
	double user;

	@Basic
	@Column(name = "`System`", nullable = false)
	double system;

}
