package ins.sino.claimcar.other.vo;

import java.io.Serializable;
import java.math.BigDecimal;

public class PrpLCheckFeeVo extends PrpLCheckFeeVoBase implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String policyNoLink;
	private String invoiceType;
	private String addTaxRate;
	private String addTaxValue;
	private String noTaxValue;
	private String taskNumber;//任务号
	private String checkname;//查勘机构名称
    private BigDecimal taskId;//工作流的taskId
    private Long acheckMainId;
    private String riskCode;
	
	public Long getAcheckMainId() {
		return acheckMainId;
	}
	public void setAcheckMainId(Long acheckMainId) {
		this.acheckMainId = acheckMainId;
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
	public String getTaskNumber() {
		return taskNumber;
	}
	public void setTaskNumber(String taskNumber) {
		this.taskNumber = taskNumber;
	}
	
	public String getCheckname() {
		return checkname;
	}
	public void setCheckname(String checkname) {
		this.checkname = checkname;
	}
	public BigDecimal getTaskId() {
		return taskId;
	}
	public void setTaskId(BigDecimal taskId) {
		this.taskId = taskId;
	}
	public String getRiskCode() {
		return riskCode;
	}
	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}
    
    

}
