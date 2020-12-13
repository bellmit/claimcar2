package ins.sino.claimcar.moblie.commonmark.caseDetails.vo;


import java.math.BigDecimal;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("EVALREPAIRSUMINFO")
public class DefLossEvalRepairSumInfoVo {

	private static final long serialVersionUID = 8423652723600188374L;
	@XStreamAlias("WORKTYPECODE")
	private String workTypeCode;  //工种编码
	@XStreamAlias("ITEMCOUNT")
	private Integer itemCount;  //项目数量
	@XStreamAlias("REFERENCEPRICE")
	private BigDecimal referencePrice;  //参考工时费
	@XStreamAlias("HOURDISCOUNT")
	private BigDecimal hourDiscount;  //工种折扣
	@XStreamAlias("DISCOUNTREFPRICE")
	private BigDecimal discountRefPrice;  //折后参考工时费
	@XStreamAlias("EVALREPAIRSUM")
	private BigDecimal evalRepairSum;  //定损工时费
	@XStreamAlias("LOSSFEE2")
	private BigDecimal lossFee2;//修理核损费用
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
	public BigDecimal getLossFee2() {
		return lossFee2;
	}
	public void setLossFee2(BigDecimal lossFee2) {
		this.lossFee2 = lossFee2;
	}
	
	
}
