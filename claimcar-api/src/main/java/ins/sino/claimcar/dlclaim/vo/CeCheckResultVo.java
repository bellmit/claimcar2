package ins.sino.claimcar.dlclaim.vo;

import java.io.Serializable;

public class CeCheckResultVo implements Serializable{
private static final long serialVersionUID = 1L;
private String claim_notification_no;//报案号
private String estimate_task_no;//任务号
private String extractionCode;//提取码
private String isBigCase;//是否大案
private String anticipation_loss;//预判金额
private String operate_type;//业务节点
private ClaimVo claim;//车辆检测结果
private String businessType;//业务类型默认通用类型
public String getClaim_notification_no() {
	return claim_notification_no;
}
public void setClaim_notification_no(String claim_notification_no) {
	this.claim_notification_no = claim_notification_no;
}
public String getEstimate_task_no() {
	return estimate_task_no;
}
public void setEstimate_task_no(String estimate_task_no) {
	this.estimate_task_no = estimate_task_no;
}
public String getExtractionCode() {
	return extractionCode;
}
public void setExtractionCode(String extractionCode) {
	this.extractionCode = extractionCode;
}
public String getIsBigCase() {
	return isBigCase;
}
public void setIsBigCase(String isBigCase) {
	this.isBigCase = isBigCase;
}
public String getAnticipation_loss() {
	return anticipation_loss;
}
public void setAnticipation_loss(String anticipation_loss) {
	this.anticipation_loss = anticipation_loss;
}
public String getOperate_type() {
	return operate_type;
}
public void setOperate_type(String operate_type) {
	this.operate_type = operate_type;
}
public ClaimVo getClaim() {
	return claim;
}
public void setClaim(ClaimVo claim) {
	this.claim = claim;
}
public String getBusinessType() {
	return businessType;
}
public void setBusinessType(String businessType) {
	this.businessType = businessType;
}
	
	
}
