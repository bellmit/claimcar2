package ins.sino.claimcar.moblie.loss.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("EVALREPAIRSUMINFO")
public class DeflossRepairSumVo implements Serializable{
	private static final long serialVersionUID = 8423652723600188374L;
	@XStreamAlias("WORKTYPECODE")
	private String workTypeCode;//工种编码 1-喷漆 2-钣金 3-电工 4-机修 5-拆装
	@XStreamAlias("ITEMCOUNT")
	private int itemCount;//项目数量
	@XStreamAlias("REFERENCEPRICE")
	private BigDecimal referencePrice;//参考工时费
	@XStreamAlias("HOURDISCOUNT")
	private BigDecimal hourDiscount;//工种折扣
	@XStreamAlias("DISCOUNTREFPRICE")
	private String discountRefPrice;//折后参考工时费
	@XStreamAlias("EVALREPAIRSUM")
	private String evalRepairSum;//定损工时费
	
	public String getWorkTypeCode() {
		return workTypeCode;
	}
	public void setWorkTypeCode(String workTypeCode) {
		this.workTypeCode = workTypeCode;
	}
	public int getItemCount() {
		return itemCount;
	}
	public void setItemCount(int itemCount) {
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
	public String getDiscountRefPrice() {
		return discountRefPrice;
	}
	public void setDiscountRefPrice(String discountRefPrice) {
		this.discountRefPrice = discountRefPrice;
	}
	public String getEvalRepairSum() {
		return evalRepairSum;
	}
	public void setEvalRepairSum(String evalRepairSum) {
		this.evalRepairSum = evalRepairSum;
	}
}
