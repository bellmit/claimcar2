package ins.sino.claimcar.ciitc.vo;

import java.io.Serializable;
import java.util.Date;

public class PrplZBXPushInfoVoBase implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Long id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
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
	
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public String getAcciNo() {
		return acciNo;
	}
	public void setAcciNo(String acciNo) {
		this.acciNo = acciNo;
	}
	public String getAcciAreaCode() {
		return acciAreaCode;
	}
	public void setAcciAreaCode(String acciAreaCode) {
		this.acciAreaCode = acciAreaCode;
	}
	public String getCaseFlag() {
		return caseFlag;
	}
	public void setCaseFlag(String caseFlag) {
		this.caseFlag = caseFlag;
	}
	public String getAcciDuty() {
		return acciDuty;
	}
	public void setAcciDuty(String acciDuty) {
		this.acciDuty = acciDuty;
	}
	public String getComAreaCode() {
		return comAreaCode;
	}
	public void setComAreaCode(String comAreaCode) {
		this.comAreaCode = comAreaCode;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public String getLicenseType() {
		return licenseType;
	}
	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}
	public String getEngineNo() {
		return engineNo;
	}
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	public String getCaseSource() {
		return caseSource;
	}
	public void setCaseSource(String caseSource) {
		this.caseSource = caseSource;
	}
	public String getIsInsured() {
		return isInsured;
	}
	public void setIsInsured(String isInsured) {
		this.isInsured = isInsured;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getUpdateUser() {
		return updateUser;
	}
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
