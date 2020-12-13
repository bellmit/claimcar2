package ins.sino.claimcar.claim.po;

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

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * POJO Class PrpLLawSuit
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PRPLLAWSUIT_PK", allocationSize = 10)
@Table(name = "PRPLLAWSUIT")
public class PrpLLawSuit implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String registNo;
	private String nodeCode;
	private String licenseNo;
	private String plainTiff;
	private String accused;
	private Date subpoenaTime;
	private String lawsuitType;
	private String lawsuitWay;
	private String createUser;
	private Date createTime;
	private BigDecimal amount;
	private BigDecimal estAmount;
	private String handleType;
	private String ciName;
	private String ttriallevel;
	private String firmname;
	private String lawyers;
	private BigDecimal attorneyfee;
	private String evaluation;
	private String litigationResult;
	private String legalduty;
	private String contentes;
	private String endReport;
	private String endUser;
	private Date endTime;
	private String courtNo;

	// 胜败诉
	private String winOrLostLawsuit;
	// 我司出庭人员
	private String toCourtPerson;
	// 是否出庭应诉
	private String isToCourt;
	// 未出庭原因
	private String unCourtReason;
	// 判决/调解金额
	private BigDecimal judgeAmount;


	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence") 
	@Column(name = "ID", unique = true, nullable = false, precision=30, scale=0)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "REGISTNO", nullable = false, length=25)
	public String getRegistNo() {
		return this.registNo;
	}



	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	@Column(name = "NODECODE", nullable = false, length=10)
	public String getNodeCode() {
		return this.nodeCode;
	}

	public void setNodeCode(String nodeCode) {
		this.nodeCode = nodeCode;
	}

	@Column(name = "LICENSENO", nullable = false, length=20)
	public String getLicenseNo() {
		return this.licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	@Column(name = "PLAINTIFF", nullable = false, length=40)
	public String getPlainTiff() {
		return this.plainTiff;
	}

	public void setPlainTiff(String plainTiff) {
		this.plainTiff = plainTiff;
	}

	@Column(name = "ACCUSED", nullable = false, length=40)
	public String getAccused() {
		return this.accused;
	}

	public void setAccused(String accused) {
		this.accused = accused;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SUBPOENATIME", length=7)
	public Date getSubpoenaTime() {
		return this.subpoenaTime;
	}

	public void setSubpoenaTime(Date subpoenaTime) {
		this.subpoenaTime = subpoenaTime;
	}

	@Column(name = "LAWSUITTYPE", length=30)
	public String getLawsuitType() {
		return this.lawsuitType;
	}

	public void setLawsuitType(String lawsuitType) {
		this.lawsuitType = lawsuitType;
	}

	@Column(name = "LAWSUITWAY", length=50)
	public String getLawsuitWay() {
		return this.lawsuitWay;
	}

	public void setLawsuitWay(String lawsuitWay) {
		this.lawsuitWay = lawsuitWay;
	}

	@Column(name = "CREATEUSER", length=12)
	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME", length=7)
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Column(name = "AMOUNT", precision=14)
	public BigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	@Column(name = "ESTAMOUNT", precision=14)
	public BigDecimal getEstAmount() {
		return this.estAmount;
	}

	public void setEstAmount(BigDecimal estAmount) {
		this.estAmount = estAmount;
	}

	@Column(name = "HANDLETYPE", length=20)
	public String getHandleType() {
		return this.handleType;
	}

	public void setHandleType(String handleType) {
		this.handleType = handleType;
	}

	@Column(name = "CINAME", length=50)
	public String getCiName() {
		return this.ciName;
	}

	public void setCiName(String ciName) {
		this.ciName = ciName;
	}

	@Column(name = "TTRIALLEVEL", length=10)
	public String getTtriallevel() {
		return this.ttriallevel;
	}

	public void setTtriallevel(String ttriallevel) {
		this.ttriallevel = ttriallevel;
	}

	@Column(name = "FIRMNAME", length=50)
	public String getFirmname() {
		return this.firmname;
	}

	public void setFirmname(String firmname) {
		this.firmname = firmname;
	}

	@Column(name = "LAWYERS", length=50)
	public String getLawyers() {
		return this.lawyers;
	}

	public void setLawyers(String lawyers) {
		this.lawyers = lawyers;
	}

	@Column(name = "ATTORNEYFEE", precision=14)
	public BigDecimal getAttorneyfee() {
		return this.attorneyfee;
	}

	public void setAttorneyfee(BigDecimal attorneyfee) {
		this.attorneyfee = attorneyfee;
	}

	@Column(name = "EVALUATION", length=20)
	public String getEvaluation() {
		return this.evaluation;
	}

	public void setEvaluation(String evaluation) {
		this.evaluation = evaluation;
	}

	@Column(name = "LITIGATIONRESULT", length=20)
	public String getLitigationResult() {
		return this.litigationResult;
	}

	public void setLitigationResult(String litigationResult) {
		this.litigationResult = litigationResult;
	}

	@Column(name = "LEGALDUTY", length=30)
	public String getLegalduty() {
		return this.legalduty;
	}

	public void setLegalduty(String legalduty) {
		this.legalduty = legalduty;
	}

	@Column(name = "CONTENTES", length=500)
	public String getContentes() {
		return this.contentes;
	}

	public void setContentes(String contentes) {
		this.contentes = contentes;
	}

	@Column(name = "ENDREPORT", length=500)
	public String getEndReport() {
		return this.endReport;
	}

	public void setEndReport(String endReport) {
		this.endReport = endReport;
	}

	@Column(name = "ENDUSER", length=12)
	public String getEndUser() {
		return this.endUser;
	}

	public void setEndUser(String endUser) {
		this.endUser = endUser;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENDTIME", length=7)
	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@Column(name = "COURTNO", length=12)
	public String getCourtNo() {
		return courtNo;
	}

	public void setCourtNo(String courtNo) {
		this.courtNo = courtNo;
	}

	@Column(name = "WINORLOSTLAWSUIT", length = 10)
	public String getWinOrLostLawsuit() {
		return winOrLostLawsuit;
	}

	public void setWinOrLostLawsuit(String winOrLostLawsuit) {
		this.winOrLostLawsuit = winOrLostLawsuit;
	}

	@Column(name = "TOCOURTPERSON", length = 50)
	public String getToCourtPerson() {
		return toCourtPerson;
	}

	public void setToCourtPerson(String toCourtPerson) {
		this.toCourtPerson = toCourtPerson;
	}

	@Column(name = "ISTOCOURT", length = 10)
	public String getIsToCourt() {
		return isToCourt;
	}

	public void setIsToCourt(String isToCourt) {
		this.isToCourt = isToCourt;
	}

	@Column(name = "UNCOURTRESON", length = 1000)
	public String getUnCourtReason() {
		return unCourtReason;
	}

	public void setUnCourtReason(String unCourtReason) {
		this.unCourtReason = unCourtReason;
	}

	@Column(name = "JUDGEAMOUNT", precision=14)
	public BigDecimal getJudgeAmount() {
		return judgeAmount;
	}

	public void setJudgeAmount(BigDecimal judgeAmount) {
		this.judgeAmount = judgeAmount;
	}
}
