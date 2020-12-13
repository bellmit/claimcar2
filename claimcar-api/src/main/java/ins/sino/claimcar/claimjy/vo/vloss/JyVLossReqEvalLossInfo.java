package ins.sino.claimcar.claimjy.vo.vloss;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("EvalLossInfo") 
public class JyVLossReqEvalLossInfo  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("LossNo") 
    private String  lossNo="";	//定损单号

	@XStreamAlias("ReportCode") 
    private String  reportCode="";	//报案号

	@XStreamAlias("DmgVhclId") 
    private String  dmgVhclId="";	//车损标的主键

	@XStreamAlias("ApprComCode") 
    private String  apprComCode="";	//核损员所属分机构代码

	@XStreamAlias("ApprCompany") 
    private String  apprCompany="";	//核损员所属分机构名称

	@XStreamAlias("ApprBranchComCode") 
    private String  apprBranchComCode="";	//核损员所属中支代码

	@XStreamAlias("ApprBranchComName") 
    private String  apprBranchComName="";	//核损员所属中支名称

	@XStreamAlias("ApprHandlerCode") 
    private String  apprHandlerCode="";	//核损员代码

	@XStreamAlias("ApprHandlerName") 
    private String  apprHandlerName="";	//核损员名称

	@XStreamAlias("EvalRemark") 
    private String  evalRemark="";	//定损单备注

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

	public String getApprComCode() {
		return apprComCode;
	}

	public void setApprComCode(String apprComCode) {
		this.apprComCode = apprComCode;
	}

	public String getApprCompany() {
		return apprCompany;
	}

	public void setApprCompany(String apprCompany) {
		this.apprCompany = apprCompany;
	}

	public String getApprBranchComCode() {
		return apprBranchComCode;
	}

	public void setApprBranchComCode(String apprBranchComCode) {
		this.apprBranchComCode = apprBranchComCode;
	}

	public String getApprBranchComName() {
		return apprBranchComName;
	}

	public void setApprBranchComName(String apprBranchComName) {
		this.apprBranchComName = apprBranchComName;
	}

	public String getApprHandlerCode() {
		return apprHandlerCode;
	}

	public void setApprHandlerCode(String apprHandlerCode) {
		this.apprHandlerCode = apprHandlerCode;
	}

	public String getApprHandlerName() {
		return apprHandlerName;
	}

	public void setApprHandlerName(String apprHandlerName) {
		this.apprHandlerName = apprHandlerName;
	}

	public String getEvalRemark() {
		return evalRemark;
	}

	public void setEvalRemark(String evalRemark) {
		this.evalRemark = evalRemark;
	}
	
	

}
