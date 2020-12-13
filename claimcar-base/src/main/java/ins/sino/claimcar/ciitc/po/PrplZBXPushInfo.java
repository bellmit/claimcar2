package ins.sino.claimcar.ciitc.po;

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
@Table(name = "PRPLZBXPUSHINFO")
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PK_PRPLZBXPUSHINFO", allocationSize = 10)
public class PrplZBXPushInfo implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private Long id;
	private String registNo;//报案号
	private String acciNo;//事故编号
	private String acciAreaCode;//事故地区代码	
	private String caseFlag;//业务类型
	private String acciDuty;//事故责任
	private String comAreaCode;//承保公司所在地区代码（省级）
	private String licenseNo;//车辆号牌号码
	private String licenseType;//车辆号牌种类
	private String engineNo;//车辆发动机号
	private String vin;//车辆车架号
	private String reportDate;//事故发生时间 格式: YYYYMMDDHHMMSS
	private String caseSource;//案件来源 -01交警在线app-02北京交警app-03北京交警警用版-04北京交警短信版
	//-05北京交警微信版-06北京交警支付宝版-07其他-08交警12123-09地方自建平台
	private String isInsured;
	private String createUser;
	private Date createTime;
	private String updateUser;
	private Date updateTime;
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence")
	@Column(name = "ID", unique = true, nullable = false, precision=15, scale=0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "REGISTNO", length = 25)
	public String getRegistNo() {
		return registNo;
	}
	
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	
	@Column(name = "ACCINO", length = 50)
	public String getAcciNo() {
		return acciNo;
	}
	
	public void setAcciNo(String acciNo) {
		this.acciNo = acciNo;
	}
	
	@Column(name = "ACCIAREACODE", length = 10)
	public String getAcciAreaCode() {
		return acciAreaCode;
	}
	
	public void setAcciAreaCode(String acciAreaCode) {
		this.acciAreaCode = acciAreaCode;
	}
	
	@Column(name = "CASEFLAG", length = 2)
	public String getCaseFlag() {
		return caseFlag;
	}
	
	public void setCaseFlag(String caseFlag) {
		this.caseFlag = caseFlag;
	}
	
	@Column(name = "ACCIDUTY", length = 2)
	public String getAcciDuty() {
		return acciDuty;
	}
	
	public void setAcciDuty(String acciDuty) {
		this.acciDuty = acciDuty;
	}
	
	@Column(name = "COMAREACODE", length = 10)
	public String getComAreaCode() {
		return comAreaCode;
	}
	
	public void setComAreaCode(String comAreaCode) {
		this.comAreaCode = comAreaCode;
	}
	
	@Column(name = "LICENSENO", length = 20)
	public String getLicenseNo() {
		return licenseNo;
	}
	
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	
	@Column(name = "LICENSETYPE", length = 2)
	public String getLicenseType() {
		return licenseType;
	}
	
	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}
	
	@Column(name = "ENGINENO", length = 50)
	public String getEngineNo() {
		return engineNo;
	}
	
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}
	
	@Column(name = "VIN", length = 50)
	public String getVin() {
		return vin;
	}
	
	public void setVin(String vin) {
		this.vin = vin;
	}
	
	@Column(name = "REPORTDATE", length = 20)
	public String getReportDate() {
		return reportDate;
	}
	
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	
	@Column(name = "CASESOURCE", length = 2)
	public String getCaseSource() {
		return caseSource;
	}
	
	public void setCaseSource(String caseSource) {
		this.caseSource = caseSource;
	}
	
	@Column(name = "ISINSURED", length = 1)
	public String getIsInsured() {
		return isInsured;
	}

	public void setIsInsured(String isInsured) {
		this.isInsured = isInsured;
	}

	@Column(name = "CREATEUSER", length = 10)
	public String getCreateUser() {
		return createUser;
	}
	
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATETIME", length = 7)
	public Date getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	@Column(name = "UPDATEUSER", length = 10)
	public String getUpdateUser() {
		return updateUser;
	}
	
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATETIME", length = 7)
	public Date getUpdateTime() {
		return updateTime;
	}
	
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}
