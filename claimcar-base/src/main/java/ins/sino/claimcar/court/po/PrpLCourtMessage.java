package ins.sino.claimcar.court.po;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PK_PRPLCOURTMESSAGE", allocationSize = 10)
@Table(name = "PRPLCOURTMESSAGE")
public class PrpLCourtMessage implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;	
	private String areaCode;
	private String caseNo;
	private String searchCode;
	private String dutyNo;
	private String acciNo;
	private Date reportDate;
	private String acciAddress;
	private String acciResult;
	private String acciType;
	private String jkptUuid;
	private String userName;
	private String passWord;
	private Date createTime;
	private Date executeTime;
	private int executeTimes;
	private String retCode;
	private String retMessage;
	private PrpLCourtAccident prpLCourtAccident;
	private List<PrpLCourtParty> prpLCourtPartys;
	private List<PrpLCourtAgent> prpLCourtAgents;
	private List<PrpLCourtIdentify> prpLCourtIdentifys;
	private List<PrpLCourtMediation> prpLCourtMediations;
	private List<PrpLCourtCompensation> prpLCourtCompensations;
	private List<PrpLCourtClaim> prpLCourtClaims;
	private List<PrpLCourtLitigation> prpLCourtLitigations;
	private PrpLCourtRegist prpLCourtRegist;
	private List<PrpLCourtConfirm> prpLCourtConfirms;
	private List<PrpLCourtFile> prpLCourtFiles;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "prpLCourtMessage")
	public PrpLCourtAccident getPrpLCourtAccident() {
		return prpLCourtAccident;
	}
	public void setPrpLCourtAccident(PrpLCourtAccident prpLCourtAccident) {
		this.prpLCourtAccident = prpLCourtAccident;
	}
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "prpLCourtMessage")
	public List<PrpLCourtParty> getPrpLCourtPartys() {
		return prpLCourtPartys;
	}
	public void setPrpLCourtPartys(List<PrpLCourtParty> prpLCourtPartys) {
		this.prpLCourtPartys = prpLCourtPartys;
	}
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "prpLCourtMessage")
	public List<PrpLCourtAgent> getPrpLCourtAgents() {
		return prpLCourtAgents;
	}
	public void setPrpLCourtAgents(List<PrpLCourtAgent> prpLCourtAgents) {
		this.prpLCourtAgents = prpLCourtAgents;
	}
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "prpLCourtMessage")
	public List<PrpLCourtIdentify> getPrpLCourtIdentifys() {
		return prpLCourtIdentifys;
	}
	public void setPrpLCourtIdentifys(List<PrpLCourtIdentify> prpLCourtIdentifys) {
		this.prpLCourtIdentifys = prpLCourtIdentifys;
	}
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "prpLCourtMessage")
	public List<PrpLCourtMediation> getPrpLCourtMediations() {
		return prpLCourtMediations;
	}
	public void setPrpLCourtMediations(List<PrpLCourtMediation> prpLCourtMediations) {
		this.prpLCourtMediations = prpLCourtMediations;
	}
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "prpLCourtMessage")
	public List<PrpLCourtCompensation> getPrpLCourtCompensations() {
		return prpLCourtCompensations;
	}
	public void setPrpLCourtCompensations(
			List<PrpLCourtCompensation> prpLCourtCompensations) {
		this.prpLCourtCompensations = prpLCourtCompensations;
	}
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "prpLCourtMessage")
	public List<PrpLCourtClaim> getPrpLCourtClaims() {
		return prpLCourtClaims;
	}
	public void setPrpLCourtClaims(List<PrpLCourtClaim> prpLCourtClaims) {
		this.prpLCourtClaims = prpLCourtClaims;
	}
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "prpLCourtMessage")
	public List<PrpLCourtLitigation> getPrpLCourtLitigations() {
		return prpLCourtLitigations;
	}
	public void setPrpLCourtLitigations(
			List<PrpLCourtLitigation> prpLCourtLitigations) {
		this.prpLCourtLitigations = prpLCourtLitigations;
	}
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "prpLCourtMessage")
	public PrpLCourtRegist getPrpLCourtRegist() {
		return prpLCourtRegist;
	}
	public void setPrpLCourtRegist(PrpLCourtRegist prpLCourtRegist) {
		this.prpLCourtRegist = prpLCourtRegist;
	}
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "prpLCourtMessage")
	public List<PrpLCourtConfirm> getPrpLCourtConfirms() {
		return prpLCourtConfirms;
	}
	public void setPrpLCourtConfirms(List<PrpLCourtConfirm> prpLCourtConfirms) {
		this.prpLCourtConfirms = prpLCourtConfirms;
	}
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "prpLCourtMessage")
	public List<PrpLCourtFile> getPrpLCourtFiles() {
		return prpLCourtFiles;
	}
	public void setPrpLCourtFiles(List<PrpLCourtFile> prpLCourtFiles) {
		this.prpLCourtFiles = prpLCourtFiles;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence")
	@Column(name = "ID", unique = true, nullable = false, precision=12, scale=0)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "AREACODE")
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	@Column(name = "CASENO")
	public String getCaseNo() {
		return caseNo;
	}
	public void setCaseNo(String caseNo) {
		this.caseNo = caseNo;
	}
	@Column(name = "SEARCHCODE")
	public String getSearchCode() {
		return searchCode;
	}
	public void setSearchCode(String searchCode) {
		this.searchCode = searchCode;
	}
	@Column(name = "DUTYNO")
	public String getDutyNo() {
		return dutyNo;
	}
	public void setDutyNo(String dutyNo) {
		this.dutyNo = dutyNo;
	}
	@Column(name = "ACCINO")
	public String getAcciNo() {
		return acciNo;
	}
	public void setAcciNo(String acciNo) {
		this.acciNo = acciNo;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REPORTDATE", length=7)
	public Date getReportDate() {
		return reportDate;
	}
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	@Column(name = "ACCIADDRESS")
	public String getAcciAddress() {
		return acciAddress;
	}
	public void setAcciAddress(String acciAddress) {
		this.acciAddress = acciAddress;
	}
	@Column(name = "ACCIRESULT")
	public String getAcciResult() {
		return acciResult;
	}
	public void setAcciResult(String acciResult) {
		this.acciResult = acciResult;
	}
	@Column(name = "ACCITYPE")
	public String getAcciType() {
		return acciType;
	}
	public void setAcciType(String acciType) {
		this.acciType = acciType;
	}
	@Column(name = "JKPTUUID")
	public String getJkptUuid() {
		return jkptUuid;
	}
	public void setJkptUuid(String jkptUuid) {
		this.jkptUuid = jkptUuid;
	}
	@Column(name = "USERNAME")
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Column(name = "PASSWORD")
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME", length=7)
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EXECUTETIME", length=7)
	public Date getExecuteTime() {
		return executeTime;
	}
	public void setExecuteTime(Date executeTime) {
		this.executeTime = executeTime;
	}
	@Column(name = "EXECUTETIMES")
	public int getExecuteTimes() {
		return executeTimes;
	}
	public void setExecuteTimes(int executeTimes) {
		this.executeTimes = executeTimes;
	}
	@Column(name = "RETCODE")
	public String getRetCode() {
		return retCode;
	}
	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}
	@Column(name = "RETMESSAGE")
	public String getRetMessage() {
		return retMessage;
	}
	public void setRetMessage(String retMessage) {
		this.retMessage = retMessage;
	}	
	
	
}
