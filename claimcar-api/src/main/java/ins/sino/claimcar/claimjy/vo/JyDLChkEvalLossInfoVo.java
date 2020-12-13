package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <pre></pre>
 * @author ★LiYi
 */
@XStreamAlias("EvalLossInfo")
public class JyDLChkEvalLossInfoVo implements Serializable {

	@XStreamAlias("LossNo")
	private String lossNo;

	@XStreamAlias("ReportCode")
	private String reportCode;

	@XStreamAlias("DmgVhclId")
	private String dmgVhclId;

	@XStreamAlias("AuditSalvageFee")
	private String auditSalvageFee;

	@XStreamAlias("AuditRemnantFee")
	private String auditRemnantFee;

	@XStreamAlias("AuditPartSum")
	private String auditPartSum;

	@XStreamAlias("AuditRepiarSum")
	private String auditRepiarSum;

	@XStreamAlias("AuditMateSum")
	private String auditMateSum;

	@XStreamAlias("TotalManageSum")
	private String totalManageSum;

	@XStreamAlias("SelfPaySum")
	private String selfPaySum;

	@XStreamAlias("OuterSum")
	private String outerSum;

	@XStreamAlias("DerogationSum")
	private String derogationSum;

	@XStreamAlias("HandlerCode")
	private String handlerCode;

	@XStreamAlias("Remark")
	private String remark;

	@XStreamAlias("TotalSum")
	private String totalSum;


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


	public String getAuditSalvageFee() {
		return auditSalvageFee;
	}

	public void setAuditSalvageFee(String auditSalvageFee) {
		this.auditSalvageFee = auditSalvageFee;
	}

	public String getAuditRemnantFee() {
		return auditRemnantFee;
	}

	public void setAuditRemnantFee(String auditRemnantFee) {
		this.auditRemnantFee = auditRemnantFee;
	}

	public String getAuditPartSum() {
		return auditPartSum;
	}

	public void setAuditPartSum(String auditPartSum) {
		this.auditPartSum = auditPartSum;
	}

	public String getAuditRepiarSum() {
		return auditRepiarSum;
	}

	public void setAuditRepiarSum(String auditRepiarSum) {
		this.auditRepiarSum = auditRepiarSum;
	}

	public String getAuditMateSum() {
		return auditMateSum;
	}

	public void setAuditMateSum(String auditMateSum) {
		this.auditMateSum = auditMateSum;
	}

	public String getTotalManageSum() {
		return totalManageSum;
	}

	public void setTotalManageSum(String totalManageSum) {
		this.totalManageSum = totalManageSum;
	}

	public String getSelfPaySum() {
		return selfPaySum;
	}

	public void setSelfPaySum(String selfPaySum) {
		this.selfPaySum = selfPaySum;
	}

	public String getOuterSum() {
		return outerSum;
	}

	public void setOuterSum(String outerSum) {
		this.outerSum = outerSum;
	}

	public String getDerogationSum() {
		return derogationSum;
	}

	public void setDerogationSum(String derogationSum) {
		this.derogationSum = derogationSum;
	}


	public String getHandlerCode() {
		return handlerCode;
	}


	public void setHandlerCode(String handlerCode) {
		this.handlerCode = handlerCode;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTotalSum() {
		return totalSum;
	}

	public void setTotalSum(String totalSum) {
		this.totalSum = totalSum;
	}



	public JyDLChkEvalLossInfoVo(){
		super();
	}

}
