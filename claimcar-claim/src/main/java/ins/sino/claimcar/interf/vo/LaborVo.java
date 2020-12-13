package ins.sino.claimcar.interf.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/*工时*/
@XStreamAlias("Labor")
public class LaborVo {
	/*保险公司定损项目编码*/
	@XStreamAlias("Code")
	private String code;
	/*工时名称*/
	@XStreamAlias("Description")
	private String description;
	/*单价*/
	@XStreamAlias("Price")
	private String price;
	/*数量*/
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
	/*工时类型*/
	@XStreamAlias("LaborType")
	private String laborType;
	/*工时所涉配件名*/
	@XStreamAlias("PartName")
	private String partName;
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
	public String getLaborType() {
		return laborType;
	}
	public void setLaborType(String laborType) {
		this.laborType = laborType;
	}
	public String getPartName() {
		return partName;
	}
	public void setPartName(String partName) {
		this.partName = partName;
	}
	
}
