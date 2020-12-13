package ins.sino.claimcar.middlestagequery.vo;

import java.util.List;

public class RespBodyVo implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	private String registNo;   //报案号
	private String policyNo;   //保单号
	private String riskCode;   //险种代码	
	private String ciPolicyNo; //交强保单号
	private String ciRiskCode; //交强险种代码
	private String damageTime;   //出险时间
	private String damageDetail; //出险原因
	private String damageAddress;//出险地址
	private String reportTime;   //报案时间
	private String reportMode;   //报案方式
	private String reportName;   //报案人
	private String linkerName;   //联系人
	private String linkerPhone;  //联系人电话
	private String damageRemark; //出险经过
	private String caseStatus;   //案件状态
	private String sumClaim;     //总估计赔款（总核损金额）
	private String sumRescueFee; //总施救费
	private String driverName;   //主驾驶员
	private String frameNo;      //车架号
	private String vinNo;        //vin码
	private String licenseNo;    //车牌号
	private String handleUser;   //处理人
	private String handleTime;   //处理时间
	private String logOffTime;   //注销时间
	private String cancelTime;   //拒赔时间
	private String endCaseTime;  //结案时间
	private List<DocCollectGuides> docCollectGuides;  //单证收集
	private List<Compensates> compensates;            //赔款信息
	private List<Payments> payments;                  //赔款支付信息
	private List<CarDefloss> carDefloss;			  //车辆核定损失信息
	
	
	
	public String getHandleUser() {
		return handleUser;
	}
	public void setHandleUser(String handleUser) {
		this.handleUser = handleUser;
	}
	public String getHandleTime() {
		return handleTime;
	}
	public void setHandleTime(String handleTime) {
		this.handleTime = handleTime;
	}
	public String getLogOffTime() {
		return logOffTime;
	}
	public void setLogOffTime(String logOffTime) {
		this.logOffTime = logOffTime;
	}
	public String getCancelTime() {
		return cancelTime;
	}
	public void setCancelTime(String cancelTime) {
		this.cancelTime = cancelTime;
	}
	public String getEndCaseTime() {
		return endCaseTime;
	}
	public void setEndCaseTime(String endCaseTime) {
		this.endCaseTime = endCaseTime;
	}
	public String getCiRiskCode() {
		return ciRiskCode;
	}
	public void setCiRiskCode(String ciRiskCode) {
		this.ciRiskCode = ciRiskCode;
	}
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getRiskCode() {
		return riskCode;
	}
	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}
	public String getCiPolicyNo() {
		return ciPolicyNo;
	}
	public void setCiPolicyNo(String ciPolicyNo) {
		this.ciPolicyNo = ciPolicyNo;
	}
	public String getDamageTime() {
		return damageTime;
	}
	public void setDamageTime(String damageTime) {
		this.damageTime = damageTime;
	}
	public String getDamageDetail() {
		return damageDetail;
	}
	public void setDamageDetail(String damageDetail) {
		this.damageDetail = damageDetail;
	}
	public String getDamageAddress() {
		return damageAddress;
	}
	public void setDamageAddress(String damageAddress) {
		this.damageAddress = damageAddress;
	}
	public String getReportTime() {
		return reportTime;
	}
	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}
	public String getReportMode() {
		return reportMode;
	}
	public void setReportMode(String reportMode) {
		this.reportMode = reportMode;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getLinkerName() {
		return linkerName;
	}
	public void setLinkerName(String linkerName) {
		this.linkerName = linkerName;
	}
	public String getLinkerPhone() {
		return linkerPhone;
	}
	public void setLinkerPhone(String linkerPhone) {
		this.linkerPhone = linkerPhone;
	}
	public String getDamageRemark() {
		return damageRemark;
	}
	public void setDamageRemark(String damageRemark) {
		this.damageRemark = damageRemark;
	}
	public String getCaseStatus() {
		return caseStatus;
	}
	public void setCaseStatus(String caseStatus) {
		this.caseStatus = caseStatus;
	}
	public String getSumClaim() {
		return sumClaim;
	}
	public void setSumClaim(String sumClaim) {
		this.sumClaim = sumClaim;
	}
	public String getSumRescueFee() {
		return sumRescueFee;
	}
	public void setSumRescueFee(String sumRescueFee) {
		this.sumRescueFee = sumRescueFee;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getFrameNo() {
		return frameNo;
	}
	public void setFrameNo(String frameNo) {
		this.frameNo = frameNo;
	}
	public String getVinNo() {
		return vinNo;
	}
	public void setVinNo(String vinNo) {
		this.vinNo = vinNo;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public List<DocCollectGuides> getDocCollectGuides() {
		return docCollectGuides;
	}
	public void setDocCollectGuides(List<DocCollectGuides> docCollectGuides) {
		this.docCollectGuides = docCollectGuides;
	}
	public List<Compensates> getCompensates() {
		return compensates;
	}
	public void setCompensates(List<Compensates> compensates) {
		this.compensates = compensates;
	}
	public List<Payments> getPayments() {
		return payments;
	}
	public void setPayments(List<Payments> payments) {
		this.payments = payments;
	}
	public List<CarDefloss> getCarDefloss() {
		return carDefloss;
	}
	public void setCarDefloss(List<CarDefloss> carDefloss) {
		this.carDefloss = carDefloss;
	}
	
	
	

}
