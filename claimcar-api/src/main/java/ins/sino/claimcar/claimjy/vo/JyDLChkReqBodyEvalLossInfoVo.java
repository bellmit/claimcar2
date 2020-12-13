package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <pre></pre>
 * @author ★LiYi
 */
@XStreamAlias("EvalLossInfo")
public class JyDLChkReqBodyEvalLossInfoVo implements Serializable {
	
	@XStreamAlias("DmgVhclId")
	private String dmgVhclId;
	
	@XStreamAlias("LossNo")
	private String lossNo;
	
	@XStreamAlias("ReportCode")
	private String reportCode;
	
	@XStreamAlias("RecheckComCode")
	private String recheckComCode;
	
	@XStreamAlias("RecheckCompany")
	private String recheckCompany;
	
	@XStreamAlias("RecheckBranchComCode")
	private String recheckBranchComCode;
	
	@XStreamAlias("RecheckBranchComName")
	private String recheckBranchComName;
	
	@XStreamAlias("RecheckHandlerCode")
	private String recheckHandlerCode;
	
	@XStreamAlias("RecheckHandlerName")
	private String recheckHandlerName;
	
	@XStreamAlias("EvalRemark")
	private String evalRemark;
	
	public String getDmgVhclId() {
		return dmgVhclId;
	}

	
	public void setDmgVhclId(String dmgVhclId) {
		this.dmgVhclId = dmgVhclId;
	}

	
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

	
	public String getRecheckComCode() {
		return recheckComCode;
	}

	
	public void setRecheckComCode(String recheckComCode) {
		this.recheckComCode = recheckComCode;
	}

	
	public String getRecheckCompany() {
		return recheckCompany;
	}

	
	public void setRecheckCompany(String recheckCompany) {
		this.recheckCompany = recheckCompany;
	}

	
	public String getRecheckBranchComCode() {
		return recheckBranchComCode;
	}

	
	public void setRecheckBranchComCode(String recheckBranchComCode) {
		this.recheckBranchComCode = recheckBranchComCode;
	}

	
	public String getRecheckBranchComName() {
		return recheckBranchComName;
	}

	
	public void setRecheckBranchComName(String recheckBranchComName) {
		this.recheckBranchComName = recheckBranchComName;
	}

	
	public String getRecheckHandlerCode() {
		return recheckHandlerCode;
	}

	
	public void setRecheckHandlerCode(String recheckHandlerCode) {
		this.recheckHandlerCode = recheckHandlerCode;
	}

	
	public String getRecheckHandlerName() {
		return recheckHandlerName;
	}

	
	public void setRecheckHandlerName(String recheckHandlerName) {
		this.recheckHandlerName = recheckHandlerName;
	}

	
	public String getEvalRemark() {
		return evalRemark;
	}

	
	public void setEvalRemark(String evalRemark) {
		this.evalRemark = evalRemark;
	}

	public JyDLChkReqBodyEvalLossInfoVo(){
		super();
	}


	
	
}
