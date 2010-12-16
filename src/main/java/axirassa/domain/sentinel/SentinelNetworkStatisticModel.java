
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
@Table(name = "SentinelNetworkStats")
public class SentinelNetworkStatisticModel {
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
	@Column(name = "`Disk`", nullable = false)
	private String disk;

	@Basic
	@Column(name = "`Used`", nullable = false)
	private long used;

	@Basic
	@Column(name = "`Total`", nullable = false)
	private long total;
}
