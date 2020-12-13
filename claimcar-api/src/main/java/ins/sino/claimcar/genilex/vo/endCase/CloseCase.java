package ins.sino.claimcar.genilex.vo.endCase;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CloseCase")
public class CloseCase implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("ReportNo") 
	private String  reportNo;
	@XStreamAlias("ClaimNo") 
	private String  claimNo;
	@XStreamAlias("ClaimAmount") 
	private String  claimAmount;
	@XStreamAlias("ClaimCloseTime") 
	private String  claimCloseTime;
	@XStreamAlias("SurveyConclusion") 
	private String  surveyConclusion;
	@XStreamAlias("ClaimType") 
	private String  claimType;
	@XStreamAlias("PayCause") 
	private String  payCause;
	@XStreamAlias("RefuseCause") 
	private String  refuseCause;
	@XStreamAlias("AccidentType") 
	private String  accidentType;
	@XStreamAlias("OtherFee") 
	private String  otherFee;
	@XStreamAlias("RevokecaseInd") 
	private String  revokecaseInd;
	@XStreamAlias("EndcaseBy") 
	private String  endcaseBy;
	@XStreamAlias("EndcaseDesc") 
	private String  endcaseDesc;
	@XStreamAlias("EndcaseDepartmentCode") 
	private String  endcaseDepartmentCode;
	@XStreamAlias("CreateBy") 
	private String  createBy;
	@XStreamAlias("CreateTime") 
	private String  createTime;
	@XStreamAlias("UpdateBy") 
	private String  updateBy;
	@XStreamAlias("UpdateTime") 
	private String  updateTime;
	
	public String getReportNo() {
		return reportNo;
	}
	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	public String getClaimAmount() {
		return claimAmount;
	}
	public void setClaimAmount(String claimAmount) {
		this.claimAmount = claimAmount;
	}
	public String getClaimCloseTime() {
		return claimCloseTime;
	}
	public void setClaimCloseTime(String claimCloseTime) {
		this.claimCloseTime = claimCloseTime;
	}
	public String getSurveyConclusion() {
		return surveyConclusion;
	}
	public void setSurveyConclusion(String surveyConclusion) {
		this.surveyConclusion = surveyConclusion;
	}
	public String getClaimType() {
		return claimType;
	}
	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}
	public String getPayCause() {
		return payCause;
	}
	public void setPayCause(String payCause) {
		this.payCause = payCause;
	}
	public String getRefuseCause() {
		return refuseCause;
	}
	public void setRefuseCause(String refuseCause) {
		this.refuseCause = refuseCause;
	}
	public String getAccidentType() {
		return accidentType;
	}
	public void setAccidentType(String accidentType) {
		this.accidentType = accidentType;
	}
	public String getOtherFee() {
		return otherFee;
	}
	public void setOtherFee(String otherFee) {
		this.otherFee = otherFee;
	}
	public String getRevokecaseInd() {
		return revokecaseInd;
	}
	public void setRevokecaseInd(String revokecaseInd) {
		this.revokecaseInd = revokecaseInd;
	}
	public String getEndcaseBy() {
		return endcaseBy;
	}
	public void setEndcaseBy(String endcaseBy) {
		this.endcaseBy = endcaseBy;
	}
	public String getEndcaseDesc() {
		return endcaseDesc;
	}
	public void setEndcaseDesc(String endcaseDesc) {
		this.endcaseDesc = endcaseDesc;
	}
	public String getEndcaseDepartmentCode() {
		return endcaseDepartmentCode;
	}
	public void setEndcaseDepartmentCode(String endcaseDepartmentCode) {
		this.endcaseDepartmentCode = endcaseDepartmentCode;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	
	
	
	
	
	
}
