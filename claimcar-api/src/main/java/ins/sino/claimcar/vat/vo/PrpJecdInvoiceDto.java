package ins.sino.claimcar.vat.vo;

import java.io.Serializable;

public class PrpJecdInvoiceDto implements Serializable{

	private static final long serialVersionUID = 1L;
	private String certiNo;//业务单号
	private String policyNo;//保单号	
	private String registNo;//报案号
	private String planFee;//收付金额
	private String payeeId;//支付对象id	
	private String lossType;//赔付类型
	private String chargeCode;//费用类型
	private String invoiceId;//vat进项发票ID
	private String invoiceType;//发票类型
	private String deductionAmount;//抵扣金额
	public String getCertiNo() {
		return certiNo;
	}
	public void setCertiNo(String certiNo) {
		this.certiNo = certiNo;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public String getPlanFee() {
		return planFee;
	}
	public void setPlanFee(String planFee) {
		this.planFee = planFee;
	}
	public String getPayeeId() {
		return payeeId;
	}
	public void setPayeeId(String payeeId) {
		this.payeeId = payeeId;
	}
	public String getLossType() {
		return lossType;
	}
	public void setLossType(String lossType) {
		this.lossType = lossType;
	}
	public String getChargeCode() {
		return chargeCode;
	}
	public void setChargeCode(String chargeCode) {
		this.chargeCode = chargeCode;
	}
	public String getInvoiceId() {
		return invoiceId;
	}
	public void setInvoiceId(String invoiceId) {
		this.invoiceId = invoiceId;
	}
	public String getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}
	public String getDeductionAmount() {
		return deductionAmount;
	}
	public void setDeductionAmount(String deductionAmount) {
		this.deductionAmount = deductionAmount;
	}


}
