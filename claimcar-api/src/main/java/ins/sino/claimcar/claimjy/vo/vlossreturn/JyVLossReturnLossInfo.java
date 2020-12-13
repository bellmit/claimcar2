package ins.sino.claimcar.claimjy.vo.vlossreturn;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement(name = "EvalLossInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class JyVLossReturnLossInfo  implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "LossNo")
	private String lossNo;

	@XmlElement(name = "ReportCode")
	private String reportCode;

	@XmlElement(name = "DmgVhclId")
	private String dmgVhclId;

	@XmlElement(name = "AuditSalvageFee")
	private String auditSalvageFee;

	@XmlElement(name = "AuditRemnantFee")
	private String auditRemnantFee;

	@XmlElement(name = "AuditPartSum")
	private String auditPartSum;

	@XmlElement(name = "AuditRepiarSum")
	private String auditRepiarSum;

	@XmlElement(name = "AuditMateSum")
	private String auditMateSum;

	@XmlElement(name = "TotalManageSum")
	private String totalManageSum;

	@XmlElement(name = "SelfPaySum")
	private String selfPaySum;

	@XmlElement(name = "OuterSum")
	private String outerSum;

	@XmlElement(name = "DerogationSum")
	private String derogationSum;

	@XmlElement(name = "HandlerCode")
	private String handlerCode;

	@XmlElement(name = "Remark")
	private String remark;
	
	@XmlElement(name = "TotalSum")
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
	
	
	

}
