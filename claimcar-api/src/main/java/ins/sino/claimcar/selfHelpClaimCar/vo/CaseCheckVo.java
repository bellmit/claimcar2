package ins.sino.claimcar.selfHelpClaimCar.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CASECHECK")
public class CaseCheckVo {
@XStreamAlias("CHECKERCODE")
private String checkerCode;//查勘处理人编码
@XStreamAlias("CHECKERNAME")	
private String checkerName;//查勘处理人名称
@XStreamAlias("CHECKTIME")	
private String checkTime;//查勘提交时间
@XStreamAlias("CHECKDESC")	
private String checkDesc;//查勘意见
@XStreamAlias("IMGREVIEW")	
private String imgreView;//资料审核意见 1.审核通过 2.审核未通过	
public String getCheckerCode() {
	return checkerCode;
}
public void setCheckerCode(String checkerCode) {
	this.checkerCode = checkerCode;
}
public String getCheckerName() {
	return checkerName;
}
public void setCheckerName(String checkerName) {
	this.checkerName = checkerName;
}
public String getCheckTime() {
	return checkTime;
}
public void setCheckTime(String checkTime) {
	this.checkTime = checkTime;
}
public String getCheckDesc() {
	return checkDesc;
}
public void setCheckDesc(String checkDesc) {
	this.checkDesc = checkDesc;
}
public String getImgreView() {
	return imgreView;
}
public void setImgreView(String imgreView) {
	this.imgreView = imgreView;
}

}
