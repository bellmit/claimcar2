package ins.sino.claimcar.losscar.vo;

import java.math.BigDecimal;


public class PrpLLossRepairSumInfoVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String workTypeCode;
	private Integer itemCount;
	private BigDecimal referencePrice;
	private BigDecimal hourDiscount;
	private BigDecimal discountRefPrice;
	private BigDecimal evalRepairSum;
	private BigDecimal veriItemCount;	//核损项目数量
	private BigDecimal apprRepairSum;	//核损工时费
	
	// 复检字段增加 1个 修理合计
	private BigDecimal dLChkApprRepairSum;// 复检工时费

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
	
	public BigDecimal getVeriItemCount() {
		return veriItemCount;
	}

	public void setVeriItemCount(BigDecimal veriItemCount) {
		this.veriItemCount = veriItemCount;
	}

	public BigDecimal getApprRepairSum() {
		return apprRepairSum;
	}

	public void setApprRepairSum(BigDecimal apprRepairSum) {
		this.apprRepairSum = apprRepairSum;
	}

	public String getWorkTypeCode() {
		return workTypeCode;
	}

	public void setWorkTypeCode(String workTypeCode) {
		this.workTypeCode = workTypeCode;
	}
	
	public Integer getItemCount() {
		return itemCount;
	}

	public void setItemCount(Integer itemCount) {
		this.itemCount = itemCount;
	}
	
	public BigDecimal getReferencePrice() {
		return referencePrice;
	}

	public void setReferencePrice(BigDecimal referencePrice) {
		this.referencePrice = referencePrice;
	}
	
	public BigDecimal getHourDiscount() {
		return hourDiscount;
	}

	public void setHourDiscount(BigDecimal hourDiscount) {
		this.hourDiscount = hourDiscount;
	}
	
	public BigDecimal getDiscountRefPrice() {
		return discountRefPrice;
	}

	public void setDiscountRefPrice(BigDecimal discountRefPrice) {
		this.discountRefPrice = discountRefPrice;
	}
	
	public BigDecimal getEvalRepairSum() {
		return evalRepairSum;
	}

	public void setEvalRepairSum(BigDecimal evalRepairSum) {
		this.evalRepairSum = evalRepairSum;
	}

	public BigDecimal getdLChkApprRepairSum() {
		return dLChkApprRepairSum;
	}

	public void setdLChkApprRepairSum(BigDecimal dLChkApprRepairSum) {
		this.dLChkApprRepairSum = dLChkApprRepairSum;
	}

}
