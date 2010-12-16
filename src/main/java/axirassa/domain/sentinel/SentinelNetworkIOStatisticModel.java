
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
@Table(name = "SentinelNetworkIOStats", uniqueConstraints = { @UniqueConstraint(columnNames = {
        "`Machine_ID`", "`Date`", "`Device`" }) })
public class SentinelNetworkIOStatisticModel {
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
	@Column(name = "`Device`", nullable = false)
	String device;

	@Basic
	@Column(name = "`Send`", nullable = false)
	long send;

	@Basic
	@Column(name = "`Receive`", nullable = false)
	long receive;
}
