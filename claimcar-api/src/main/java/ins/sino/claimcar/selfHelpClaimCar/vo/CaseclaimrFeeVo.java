package ins.sino.claimcar.selfHelpClaimCar.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CASECLAIMRFEE")
public class CaseclaimrFeeVo {
  @XStreamAlias("STIPOLICYNO")
  private String stipolicyNo;//交强险保单号
  @XStreamAlias("BUSIPOLICYNO")
  private String busipolicyNo;//商业险保单号
  @XStreamAlias("CPSMONEY")
  private String cpsMoney;//理赔总金额
  @XStreamAlias("CPSTIME")
  private String cpsTime;//理赔时间
  @XStreamAlias("CPSRESULT")
  private String cpsResult;//理赔说明
public String getCpsMoney() {
	return cpsMoney;
}
public void setCpsMoney(String cpsMoney) {
	this.cpsMoney = cpsMoney;
}
public String getCpsTime() {
	return cpsTime;
}
public void setCpsTime(String cpsTime) {
	this.cpsTime = cpsTime;
}
public String getCpsResult() {
	return cpsResult;
}
public void setCpsResult(String cpsResult) {
	this.cpsResult = cpsResult;
}
public String getStipolicyNo() {
	return stipolicyNo;
}
public void setStipolicyNo(String stipolicyNo) {
	this.stipolicyNo = stipolicyNo;
}
public String getBusipolicyNo() {
	return busipolicyNo;
}
public void setBusipolicyNo(String busipolicyNo) {
	this.busipolicyNo = busipolicyNo;
}
  

}
