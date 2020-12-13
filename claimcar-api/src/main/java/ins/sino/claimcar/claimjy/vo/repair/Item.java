package ins.sino.claimcar.claimjy.vo.repair;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("Item")
public class Item implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("BrandCode")//品牌编码
	private String brandCode = "";
	@XStreamAlias("BrandName")//品牌名称
	private String brandName = "";
	@XStreamAlias("BrandPartDiscountRate")//换件折扣率
	private String brandPartDiscountRate = "";
	@XStreamAlias("BrandRepairDiscountRate")//工时折扣率
	private String brandRepairDiscountRate = "";
	@XStreamAlias("SpecialFlag")//是否特约
	private String specialFlag = "";
	
	public String getBrandCode() {
		return brandCode;
	}
	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getBrandPartDiscountRate() {
		return brandPartDiscountRate;
	}
	public void setBrandPartDiscountRate(String brandPartDiscountRate) {
		this.brandPartDiscountRate = brandPartDiscountRate;
	}
	public String getBrandRepairDiscountRate() {
		return brandRepairDiscountRate;
	}
	public void setBrandRepairDiscountRate(String brandRepairDiscountRate) {
		this.brandRepairDiscountRate = brandRepairDiscountRate;
	}
	public String getSpecialFlag() {
		return specialFlag;
	}
	public void setSpecialFlag(String specialFlag) {
		this.specialFlag = specialFlag;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
