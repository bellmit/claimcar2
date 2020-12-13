package ins.sino.claimcar.claim.vo;

import java.math.BigDecimal;

/**
 * Custom VO class of PO PrpLLossPersonFee
 */ 
public class PrpLLossPersonFeeVo extends PrpLLossPersonFeeVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	
	private BigDecimal deductOffAmt;
	private String kindCode;
	private BigDecimal deductOffRate;
	
	private BigDecimal dutyRate;
	private BigDecimal deductDutyRate;
	private BigDecimal deductAddRate;
	/**人伤定损 跟踪人员表人员ID **/
	private Long personId;
	
	private BigDecimal itemAmount;
	private BigDecimal itemValue;
	private BigDecimal deductibleRate;
	private BigDecimal quantity;
	
	public BigDecimal getDutyRate() {
		return dutyRate;
	}

	
	public void setDutyRate(BigDecimal dutyRate) {
		this.dutyRate = dutyRate;
	}

	
	public BigDecimal getDeductDutyRate() {
		return deductDutyRate;
	}

	
	public void setDeductDutyRate(BigDecimal deductDutyRate) {
		this.deductDutyRate = deductDutyRate;
	}

	
	public BigDecimal getDeductAddRate() {
		return deductAddRate;
	}

	
	public void setDeductAddRate(BigDecimal deductAddRate) {
		this.deductAddRate = deductAddRate;
	}

	
	public BigDecimal getItemAmount() {
		return itemAmount;
	}

	
	public void setItemAmount(BigDecimal itemAmount) {
		this.itemAmount = itemAmount;
	}

	
	public BigDecimal getItemValue() {
		return itemValue;
	}

	
	public void setItemValue(BigDecimal itemValue) {
		this.itemValue = itemValue;
	}

	
	public BigDecimal getDeductibleRate() {
		return deductibleRate;
	}

	
	public void setDeductibleRate(BigDecimal deductibleRate) {
		this.deductibleRate = deductibleRate;
	}

	public BigDecimal getDeductOffAmt() {
		return deductOffAmt;
	}

	public void setDeductOffAmt(BigDecimal deductOffAmt) {
		this.deductOffAmt = deductOffAmt;
	}

	public String getKindCode() {
		return kindCode;
	}

	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}

	public BigDecimal getDeductOffRate() {
		return deductOffRate;
	}

	public void setDeductOffRate(BigDecimal deductOffRate) {
		this.deductOffRate = deductOffRate;
	}


	public Long getPersonId() {
		return personId;
	}


	public void setPersonId(Long personId) {
		this.personId = personId;
	}


	public BigDecimal getQuantity() {
		return quantity;
	}


	public void setQuantity(BigDecimal quantity) {
		this.quantity = quantity;
	}
	
}
