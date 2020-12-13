package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class LossRepairSumInfoItem implements Serializable{
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "WorkTypeCode")
	private String workTypeCode;//工种编码 1-喷漆 2-钣金 3-电工 4-机修 5-拆装
	@XmlElement(name = "ItemCount")
	private int itemCount;//项目数量
	@XmlElement(name = "ReferencePrice")
	private BigDecimal referencePrice;//参考工时费
	@XmlElement(name = "HourDiscount")
	private BigDecimal hourDiscount;//工种折扣
	@XmlElement(name = "DiscountRefPrice")
	private String discountRefPrice;//折后参考工时费
	@XmlElement(name = "EvalRepairSum")
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
