package ins.sino.claimcar.realtimequery.po;

import java.io.Serializable;
import java.math.BigDecimal;
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
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PRPLBASICSINFO_PK", allocationSize = 10)
@Table(name = "PRPLBASICSINFO")
public class PrpLBasicsInfo  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String reportNo;  //报案号
	private String claimcomPany; //保险公司
	private String insurerArea;  //承保地区
	private Date reportTime;  //报案时间
	private Date accidentTime;  //出险时间
	private String accidentPlace;  //出险地点
	private String accidentDescription;  //出险经过
	private String claimStatus;  //案件状态代码
	private String riskType;  //保单类型代码
	private String claimQueryNo;  //理赔编码
	private BigDecimal sumUnderDefLoss;  //核损总金额
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
	@Column(name = "REPORTNO", nullable = false, length=25)
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	@Column(name = "CLAIMCOMPANY", nullable = false, length=100)
	public String getClaimcomPany() {
		return claimcomPany;
	}
	public void setClaimcomPany(String claimcomPany) {
		this.claimcomPany = claimcomPany;
	}
	@Column(name = "INSURERAREA", nullable = false, length=200)
	public String getInsurerArea() {
		return insurerArea;
	}
	public void setInsurerArea(String insurerArea) {
		this.insurerArea = insurerArea;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REPORTTIME", length=20)
	public Date getReportTime() {
		return reportTime;
	}
	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ACCIDENTTIME", length=20)
	public Date getAccidentTime() {
		return accidentTime;
	}
	public void setAccidentTime(Date accidentTime) {
		this.accidentTime = accidentTime;
	}
	@Column(name = "ACCIDENTPLACE", nullable = false, length=200)
	public String getAccidentPlace() {
		return accidentPlace;
	}
	public void setAccidentPlace(String accidentPlace) {
		this.accidentPlace = accidentPlace;
	}
	@Column(name = "ACCIDENTDESCRIPTION", nullable = false, length=1000)
	public String getAccidentDescription() {
		return accidentDescription;
	}
	public void setAccidentDescription(String accidentDescription) {
		this.accidentDescription = accidentDescription;
	}
	@Column(name = "CLAIMSTATUS", nullable = false, length=2)
	public String getClaimStatus() {
		return claimStatus;
	}
	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}
	@Column(name = "RISKTYPE", nullable = false, length=2)
	public String getRiskType() {
		return riskType;
	}
	public void setRiskType(String riskType) {
		this.riskType = riskType;
	}
	@Column(name = "CLAIMQUERYNO", nullable = false, length=50)
	public String getClaimQueryNo() {
		return claimQueryNo;
	}
	public void setClaimQueryNo(String claimQueryNo) {
		this.claimQueryNo = claimQueryNo;
	}
	@Column(name = "SUMUNDERDEFLOSS", nullable = false, length=14)
	public BigDecimal getSumUnderDefLoss() {
		return sumUnderDefLoss;
	}
	public void setSumUnderDefLoss(BigDecimal sumUnderDefLoss) {
		this.sumUnderDefLoss = sumUnderDefLoss;
	}
	@Column(name = "UPPERID", nullable = false, length=13)
	public Long getUpperId() {
		return upperId;
	}
	public void setUpperId(Long upperId) {
		this.upperId = upperId;
	}
	
	
}
