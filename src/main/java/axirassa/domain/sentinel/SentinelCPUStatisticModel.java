
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

@Entity
@Table(name = "SentinelCPUStats")
public class SentinelCPUStatisticModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Basic
	@Column(name = "`Machine_ID`", nullable = false)
	private int machineid;

	@Basic
	@Column(name = "`Date`", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	@Basic
	@Column(name = "`Cpu`", nullable = false)
	private int cpuid;

	@Basic
	@Column(name = "`User`", nullable = false)
	private double user;

	@Basic
	@Column(name = "`System`", nullable = false)
	private double system;

}
