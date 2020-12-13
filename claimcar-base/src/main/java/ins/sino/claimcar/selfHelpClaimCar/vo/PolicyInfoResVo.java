package ins.sino.claimcar.selfHelpClaimCar.vo;


import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("POLICYINFO")
public class PolicyInfoResVo {
  @XStreamAlias("GLPOLICYNO")
  private String glpolicyNo;//关联保单号
  @XStreamAlias("POLICYNO")
  private String policyNo;//保单号
  @XStreamAlias("LICENSENO")
  private String licenseNo;//车牌号
  @XStreamAlias("RISKCODE")
  private String riskCode;//险种
  @XStreamAlias("STARTDATE")
  private String startDate;//保险起期
  @XStreamAlias("ENDDATE")
  private String endDate;//保险止期
public String getPolicyNo() {
	return policyNo;
}
public void setPolicyNo(String policyNo) {
	this.policyNo = policyNo;
}
public String getLicenseNo() {
	return licenseNo;
}
public void setLicenseNo(String licenseNo) {
	this.licenseNo = licenseNo;
}
public String getRiskCode() {
	return riskCode;
}
public void setRiskCode(String riskCode) {
	this.riskCode = riskCode;
}
public String getStartDate() {
	return startDate;
}
public void setStartDate(String startDate) {
	this.startDate = startDate;
}
public String getEndDate() {
	return endDate;
}
public void setEndDate(String endDate) {
	this.endDate = endDate;
}
public String getGlpolicyNo() {
	return glpolicyNo;
}
public void setGlpolicyNo(String glpolicyNo) {
	this.glpolicyNo = glpolicyNo;
}

}
