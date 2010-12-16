
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
@Table(name = "SentinelDiskIOStats", uniqueConstraints = { @UniqueConstraint(columnNames = {
        "`Machine_ID`", "`Date`", "`Disk`" }) })
public class SentinelDiskIOStatisticModel implements SentinelStatisticModel {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	@Basic
	@Column(name = "`Machine_ID`", nullable = false)
	public int machineid;

	@Basic
	@Column(name = "`Date`", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date date;

	@Basic
	@Column(name = "`Disk`", nullable = false)
	public String disk;

	@Basic
	@Column(name = "`Read`", nullable = false)
	public float read;

	@Basic
	@Column(name = "`Write`", nullable = false)
	public float write;
}
