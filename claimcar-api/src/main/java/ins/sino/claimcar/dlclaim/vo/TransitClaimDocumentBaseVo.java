package ins.sino.claimcar.dlclaim.vo;

import java.io.Serializable;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransitClaimDocumentBaseVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
  private String claim_notification_no;//报案号 
  private String estimate_company_code;//保险公司 
  private String estimate_company_id;//定损员所在机构
  private List<PolicyVo> policy;//保单信息集合
  private ReportVo report;//报案信息
  private CheckVo check;//查勘信息
  private ConfirmLossVo confirmLoss;//定损信息
  private List<ClaimPhotosVo> claimPhotos;
  private String operate_type;
  private String businessType;
  private String regist_type;//报文类型
  
public String getClaim_notification_no() {
	return claim_notification_no;
}
public void setClaim_notification_no(String claim_notification_no) {
	this.claim_notification_no = claim_notification_no;
}
public String getEstimate_company_code() {
	return estimate_company_code;
}
public void setEstimate_company_code(String estimate_company_code) {
	this.estimate_company_code = estimate_company_code;
}
public String getEstimate_company_id() {
	return estimate_company_id;
}
public void setEstimate_company_id(String estimate_company_id) {
	this.estimate_company_id = estimate_company_id;
}

public ReportVo getReport() {
	return report;
}
public void setReport(ReportVo report) {
	this.report = report;
}
public CheckVo getCheck() {
	return check;
}
public void setCheck(CheckVo check) {
	this.check = check;
}
public ConfirmLossVo getConfirmLoss() {
	return confirmLoss;
}
public void setConfirmLoss(ConfirmLossVo confirmLoss) {
	this.confirmLoss = confirmLoss;
}
public List<ClaimPhotosVo> getClaimPhotos() {
	return claimPhotos;
}
public void setClaimPhotos(List<ClaimPhotosVo> claimPhotos) {
	this.claimPhotos = claimPhotos;
}
public String getOperate_type() {
	return operate_type;
}
public void setOperate_type(String operate_type) {
	this.operate_type = operate_type;
}
public List<PolicyVo> getPolicy() {
	return policy;
}
public void setPolicy(List<PolicyVo> policy) {
	this.policy = policy;
}
public String getBusinessType() {
	return businessType;
}
public void setBusinessType(String businessType) {
	this.businessType = businessType;
}
public String getRegist_type() {
	return regist_type;
}
public void setRegist_type(String regist_type) {
	this.regist_type = regist_type;
}


  
  
}
