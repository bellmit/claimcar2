package ins.sino.claimcar.realtimequery.po;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PRPLINJUREDDETAILS_PK", allocationSize = 10)
@Table(name = "PRPLINJUREDDETAILS")
public class PrpLInjuredDetails implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String injuryPart;  //受伤部位	
	private String injuryLevelCode;  //伤残程度代码	
	private Date deathTime;  //死亡时间	
	private PrpLCasualtyInfor prpLCasualtyInfor;
	private Long upperId;  //维护一个父级关系表id
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence")
	@Column(name = "ID", unique = true, nullable = false, precision=13, scale=0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "INJURYPART", nullable = false, length=1)
	public String getInjuryPart() {
		return injuryPart;
	}
	public void setInjuryPart(String injuryPart) {
		this.injuryPart = injuryPart;
	}
	@Column(name = "INJURYLEVELCODE", nullable = false, length=1)
	public String getInjuryLevelCode() {
		return injuryLevelCode;
	}
	public void setInjuryLevelCode(String injuryLevelCode) {
		this.injuryLevelCode = injuryLevelCode;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DEATHTIME", length=20)
	public Date getDeathTime() {
		return deathTime;
	}
	public void setDeathTime(Date deathTime) {
		this.deathTime = deathTime;
	}
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="CASUALTYINFORID", nullable = false)
	public PrpLCasualtyInfor getPrpLCasualtyInfor() {
		return prpLCasualtyInfor;
	}
	public void setPrpLCasualtyInfor(PrpLCasualtyInfor prpLCasualtyInfor) {
		this.prpLCasualtyInfor = prpLCasualtyInfor;
	}            
	@Column(name = "UPPERID", nullable = false,precision=13, scale=0)
	public Long getUpperId() {
		return upperId;
	}
	public void setUpperId(Long upperId) {
		this.upperId = upperId;
	}
	
	
	
}
