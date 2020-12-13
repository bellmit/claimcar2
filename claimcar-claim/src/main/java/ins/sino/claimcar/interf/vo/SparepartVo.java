package ins.sino.claimcar.interf.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/*配件*/
@XStreamAlias("Sparepart")
public class SparepartVo {
	/*保险公司定损项目编码*/
	@XStreamAlias("Code")
	private String code;
	/*配件名称*/
	@XStreamAlias("Description")
	private String description;
	/*配件单价*/
	@XStreamAlias("Price")
	private String price;
	/*配件数量*/
	@XStreamAlias("Count")
	private String count;
	/*备注*/
	@XStreamAlias("Remark")
	private String remark;
	/*是否手动录入*/
	@XStreamAlias("HandAddFlag")
	private String handAddFlag;
	/*定损险种*/
	@XStreamAlias("InsuranceItem")
	private String insuranceItem;
	/*管理费*/
	@XStreamAlias("ManagementFee")
	private String managementFee;
	/*换件价格方案（市场/4S店）*/
	@XStreamAlias("OriginalPriceType")
	private String originalPriceType;
	/*折扣(小于1的小数)*/
	@XStreamAlias("Discount")
	private String discount;
	/*配件原厂编码（OEM码）*/
	@XStreamAlias("OEM")
	private String oem;
	/*残值*/
	@XStreamAlias("RestValue")
	private String restValue;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getHandAddFlag() {
		return handAddFlag;
	}
	public void setHandAddFlag(String handAddFlag) {
		this.handAddFlag = handAddFlag;
	}
	public String getInsuranceItem() {
		return insuranceItem;
	}
	public void setInsuranceItem(String insuranceItem) {
		this.insuranceItem = insuranceItem;
	}
	public String getManagementFee() {
		return managementFee;
	}
	public void setManagementFee(String managementFee) {
		this.managementFee = managementFee;
	}
	public String getOriginalPriceType() {
		return originalPriceType;
	}
	public void setOriginalPriceType(String originalPriceType) {
		this.originalPriceType = originalPriceType;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getOem() {
		return oem;
	}
	public void setOem(String oem) {
		this.oem = oem;
	}
	public String getRestValue() {
		return restValue;
	}
	public void setRestValue(String restValue) {
		this.restValue = restValue;
	}
	
}
