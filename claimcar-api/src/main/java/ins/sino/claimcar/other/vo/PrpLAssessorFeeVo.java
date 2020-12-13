package ins.sino.claimcar.other.vo;

import java.math.BigDecimal;

/**
 * Custom VO class of PO PrpLAssessorFee
 */ 
public class PrpLAssessorFeeVo extends PrpLAssessorFeeVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	private String policyNoLink;
	private String invoiceType;
	private String addTaxRate;
	private String addTaxValue;
	private String noTaxValue;
	private String taskNumber;//任务号
	private String intermname;//公估机构名称
    private BigDecimal taskId;//工作流的taskId
	private PrpLAssessorMainVo assessorMainVo;
	private String riskCode;
	
	public PrpLAssessorMainVo getAssessorMainVo() {
		return assessorMainVo;
	}

	public void setAssessorMainVo(PrpLAssessorMainVo assessorMainVo) {
		this.assessorMainVo = assessorMainVo;
	}

	public BigDecimal getTaskId() {
		return taskId;
	}
	public void setTaskId(BigDecimal taskId) {
		this.taskId = taskId;
	}
	public String getIntermname() {
		return intermname;
	}
	public void setIntermname(String intermname) {
		this.intermname = intermname;
	}
	public String getTaskNumber() {
		return taskNumber;
	}
	public void setTaskNumber(String taskNumber) {
		this.taskNumber = taskNumber;
	}
	private Long assessMainId;
	
	public Long getAssessMainId() {
		return assessMainId;
	}
	public void setAssessMainId(Long assessMainId) {
		this.assessMainId = assessMainId;
	}
	
	
	public String getPolicyNoLink() {
		return policyNoLink;
	}
	public void setPolicyNoLink(String policyNoLink) {
		this.policyNoLink = policyNoLink;
	}
	public String getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}
	public String getAddTaxRate() {
		return addTaxRate;
	}
	public void setAddTaxRate(String addTaxRate) {
		this.addTaxRate = addTaxRate;
	}
	public String getAddTaxValue() {
		return addTaxValue;
	}
	public void setAddTaxValue(String addTaxValue) {
		this.addTaxValue = addTaxValue;
	}
	public String getNoTaxValue() {
		return noTaxValue;
	}
	public void setNoTaxValue(String noTaxValue) {
		this.noTaxValue = noTaxValue;
	}
	public String getRiskCode() {
		return riskCode;
	}
	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}
	
}
