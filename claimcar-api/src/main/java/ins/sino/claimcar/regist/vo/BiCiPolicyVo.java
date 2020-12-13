package ins.sino.claimcar.regist.vo;

import java.util.Date;


public class BiCiPolicyVo implements java.io.Serializable{
 private static final long serialVersionUID = 1L;
 private String bipolicyNo;//商业险保单号
 private String cipolicyNo;//交强险保单号
 private Date damageTime;//出险时间
 private Date reportTime;//报案时间
 private String isDamageFlag;//是否人伤
 public String getBipolicyNo() {
	return bipolicyNo;
 }
 public void setBipolicyNo(String bipolicyNo) {
	this.bipolicyNo = bipolicyNo;
 }
 public String getCipolicyNo() {
	return cipolicyNo;
 }
 public void setCipolicyNo(String cipolicyNo) {
	this.cipolicyNo = cipolicyNo;
 }
public Date getDamageTime() {
	return damageTime;
}
public void setDamageTime(Date damageTime) {
	this.damageTime = damageTime;
}
public Date getReportTime() {
	return reportTime;
}
public void setReportTime(Date reportTime) {
	this.reportTime = reportTime;
}
public String getIsDamageFlag() {
	return isDamageFlag;
}
public void setIsDamageFlag(String isDamageFlag) {
	this.isDamageFlag = isDamageFlag;
}

}
