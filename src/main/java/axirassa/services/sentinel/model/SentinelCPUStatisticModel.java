
package axirassa.services.sentinel.model;

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
@Table(
        name = "SentinelCPUStats",
        uniqueConstraints = { @UniqueConstraint(columnNames = { "machineid", "date", "cpu" }) })
public class SentinelCPUStatisticModel implements SentinelStatisticModel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	@Basic
	@Column(nullable = false)
	public int machineid;

	@Basic
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date date;

	@Basic
	@Column(nullable = false)
	public int cpu;

	@Basic
	@Column(nullable = false)
	public double user;

	@Basic
	@Column(nullable = false)
	public double system;

}
