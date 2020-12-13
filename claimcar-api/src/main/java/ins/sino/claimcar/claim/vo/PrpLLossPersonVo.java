package ins.sino.claimcar.claim.vo;

import java.math.BigDecimal;

/**
 * Custom VO class of PO PrpLLossPerson
 */ 
public class PrpLLossPersonVo extends PrpLLossPersonVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	
	private BigDecimal quantity;

	private BigDecimal originLossFee;  //保存初始化的 定损金额 发票金额 小值

	public BigDecimal getOriginLossFee() {
		return originLossFee;
	}

	public void setOriginLossFee(BigDecimal originLossFee) {
		this.originLossFee = originLossFee;
	}

	public BigDecimal getQuantity() {
		return quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	
	
}
