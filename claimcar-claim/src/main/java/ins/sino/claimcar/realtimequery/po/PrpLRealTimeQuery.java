package ins.sino.claimcar.realtimequery.po;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PRPLREALTIMEQUERY_PK", allocationSize = 10)
@Table(name = "PRPLREALTIMEQUERY")
public class PrpLRealTimeQuery implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String usAge;  //查询剩余次数
	private String reportNo;
	private String disType; //区分接口 车辆信息接口-为1，人员信息接口-为2，报案电话接口-为3
	private Date changeTime; //接口返回时间,系统交互时间
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence")
	@Column(name = "ID", unique = true, nullable = false, precision=13, scale=0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "USAGE", nullable = false, length=10)
	public String getUsAge() {
		return usAge;
	}
	public void setUsAge(String usAge) {
		this.usAge = usAge;
	}
	@Column(name = "REPORTNO", nullable = false, length=25)
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	@Column(name = "DISTYPE", nullable = false, length=1)
	public String getDisType() {
		return disType;
	}
	public void setDisType(String disType) {
		this.disType = disType;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CHANGETIME", length=20)
	public Date getChangeTime() {
		return changeTime;
	}
	public void setChangeTime(Date changeTime) {
		this.changeTime = changeTime;
	}
	
}
