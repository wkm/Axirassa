
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
@Table(name = "SentinelNetworkIOStats")
public class SentinelNetworkIOStatisticModel {
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
	@Column(name = "`Device`", nullable = false)
	private String device;

	@Basic
	@Column(name = "`Send`", nullable = false)
	private long send;

	@Basic
	@Column(name = "`Receive`", nullable = false)
	private long receive;
}
