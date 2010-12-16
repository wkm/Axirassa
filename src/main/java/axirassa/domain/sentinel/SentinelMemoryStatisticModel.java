
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
@Table(
        name = "SentinelMemoryStats",
        uniqueConstraints = { @UniqueConstraint(columnNames = { "`Machine_ID`", "`Date`" }) })
public class SentinelMemoryStatisticModel {
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
	@Column(name = "`Used`", nullable = false)
	private long used;

	@Basic
	@Column(name = "`Total`", nullable = false)
	private long total;
}
