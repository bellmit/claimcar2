package ins.sino.claimcar.claim.vo;

import java.math.BigDecimal;

/**
 * Custom VO class of PO PrpLLossProp
 */ 
public class PrpLLossPropVo extends PrpLLossPropVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	
	private BigDecimal maxQuantity;
	
	private BigDecimal unitAmount;
	
    private BigDecimal originLossFee;  //保存初始化的 定损金额 发票金额 小值
    
    private BigDecimal originLossMaxFee;  //保存初始化的 定损金额的最大值

	public BigDecimal getMaxQuantity() {
		return maxQuantity;
	}

	public void setMaxQuantity(BigDecimal maxQuantity) {
		this.maxQuantity = maxQuantity;
	}

	public BigDecimal getUnitAmount() {
		return unitAmount;
	}

	public void setUnitAmount(BigDecimal unitAmount) {
		this.unitAmount = unitAmount;
	}

	public BigDecimal getOriginLossFee() {
		return originLossFee;
	}

	public void setOriginLossFee(BigDecimal originLossFee) {
		this.originLossFee = originLossFee;
	}

	public BigDecimal getOriginLossMaxFee() {
		return originLossMaxFee;
	}

	public void setOriginLossMaxFee(BigDecimal originLossMaxFee) {
		this.originLossMaxFee = originLossMaxFee;
	}

	
	
	
}
