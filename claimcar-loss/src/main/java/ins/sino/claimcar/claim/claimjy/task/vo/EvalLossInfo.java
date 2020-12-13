package ins.sino.claimcar.claim.claimjy.task.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("EvalLossInfo")
public class EvalLossInfo implements Serializable{
	private static final long serialVersionUID = 1L;

	@XStreamAlias("LossNo")//定损单号
	private String lossNo="";
	@XStreamAlias("ReportCode")//报案号
	private String reportCode="";
	@XStreamAlias("DmgVhclId")//车损标的主键
	private String dmgVhclId="";
	@XStreamAlias("EvalLossType")//定损单类型
	private String evalLossType="";
	@XStreamAlias("StatusCode")//状态代码
	private String statusCode="";
	@XStreamAlias("StatusName")//状态名称
	private String statusName="";
	@XStreamAlias("ComCode")//操作人所属分机构代码
	private String comCode="";
	@XStreamAlias("Company")//操作人所属分机构名称
	private String company="";
	@XStreamAlias("BranchComCode")//操作人所属中支代码
	private String branchComCode="";
	@XStreamAlias("BranchComName")//操作人所属中支名称
	private String branchComName="";
	@XStreamAlias("HandlerCode")//操作人代码
	private String handlerCode="";
	@XStreamAlias("HandlerName")//操作人名称
	private String handlerName="";
	@XStreamAlias("OperationLink")//操作环节
	private String operationLink="";
	@XStreamAlias("OperationResults")//操作结果
	private String operationResults="";
	@XStreamAlias("OperationOpinion")//整单备注
	private String operationOpinion="";
	@XStreamAlias("SelfEstiFlag")//自核价标记
	private String selfEstiFlag="";
	@XStreamAlias("SelfApproveFlag")//自核损标记
	private String selfApproveFlag="";
	public String getLossNo() {
		return lossNo;
	}
	public void setLossNo(String lossNo) {
		this.lossNo = lossNo;
	}
	public String getReportCode() {
		return reportCode;
	}
	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}
	public String getDmgVhclId() {
		return dmgVhclId;
	}
	public void setDmgVhclId(String dmgVhclId) {
		this.dmgVhclId = dmgVhclId;
	}
	public String getEvalLossType() {
		return evalLossType;
	}
	public void setEvalLossType(String evalLossType) {
		this.evalLossType = evalLossType;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getComCode() {
		return comCode;
	}
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getBranchComCode() {
		return branchComCode;
	}
	public void setBranchComCode(String branchComCode) {
		this.branchComCode = branchComCode;
	}
	public String getBranchComName() {
		return branchComName;
	}
	public void setBranchComName(String branchComName) {
		this.branchComName = branchComName;
	}
	public String getHandlerCode() {
		return handlerCode;
	}
	public void setHandlerCode(String handlerCode) {
		this.handlerCode = handlerCode;
	}
	public String getHandlerName() {
		return handlerName;
	}
	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}
	public String getOperationLink() {
		return operationLink;
	}
	public void setOperationLink(String operationLink) {
		this.operationLink = operationLink;
	}
	public String getOperationResults() {
		return operationResults;
	}
	public void setOperationResults(String operationResults) {
		this.operationResults = operationResults;
	}
	public String getOperationOpinion() {
		return operationOpinion;
	}
	public void setOperationOpinion(String operationOpinion) {
		this.operationOpinion = operationOpinion;
	}
	public String getSelfEstiFlag() {
		return selfEstiFlag;
	}
	public void setSelfEstiFlag(String selfEstiFlag) {
		this.selfEstiFlag = selfEstiFlag;
	}
	public String getSelfApproveFlag() {
		return selfApproveFlag;
	}
	public void setSelfApproveFlag(String selfApproveFlag) {
		this.selfApproveFlag = selfApproveFlag;
	}
}
