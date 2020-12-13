package ins.sino.claimcar.newpayment.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


public class InvoiceDetailVo implements Serializable{


	private static final long serialVersionUID = 1L;

	/** 业务号*/
	private String certiNo;
	/** 保单号*/
	private String policyNo;
	/** 报案号*/
	private String registNo;;
	/** 险种代码*/
	private String riskCode;
	/** 赔款金额*/
	private BigDecimal planFee;
	/** 支付对象代码*/
	private String payeeId;
	/** 赔付类型*/
	private String lossType;
	/** 费用类型*/
	private String chargeCode;
	/** vat进项发票ID*/
	private String invoiceId;
	/** 抵扣金额*/
	private BigDecimal deductionAmount;
	/** 价税拆分险别信息*/
	private List<PrpJlossPlanSubVatVo> prpJlossPlanSubDtos;
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
	public String getRiskCode() {
		return riskCode;
	}
	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}
	public BigDecimal getPlanFee() {
		return planFee;
	}
	public void setPlanFee(BigDecimal planFee) {
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
	
	public BigDecimal getDeductionAmount() {
		return deductionAmount;
	}
	public void setDeductionAmount(BigDecimal deductionAmount) {
		this.deductionAmount = deductionAmount;
	}
	public List<PrpJlossPlanSubVatVo> getPrpJlossPlanSubDtos() {
		return prpJlossPlanSubDtos;
	}
	public void setPrpJlossPlanSubDtos(
			List<PrpJlossPlanSubVatVo> prpJlossPlanSubDtos) {
		this.prpJlossPlanSubDtos = prpJlossPlanSubDtos;
	}
	
	
}