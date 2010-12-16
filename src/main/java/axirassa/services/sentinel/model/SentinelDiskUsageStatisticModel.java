
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
@Table(name = "SentinelDiskUsageStats", uniqueConstraints = { @UniqueConstraint(columnNames = {
        "`Machine_ID`", "`Date`", "`Disk`" }) })
public class SentinelDiskUsageStatisticModel implements SentinelModel {
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
	@Column(name = "`Disk`", nullable = false)
	String disk;

	@Basic
	@Column(name = "`Used`", nullable = false)
	long used;

	@Basic
	@Column(name = "`Total`", nullable = false)
	long total;
}
