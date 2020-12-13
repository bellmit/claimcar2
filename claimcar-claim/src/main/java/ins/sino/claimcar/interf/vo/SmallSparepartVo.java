package ins.sino.claimcar.interf.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/*辅料*/
@XStreamAlias("SmallSparepart")
public class SmallSparepartVo {
	/*保险公司定损项目编码*/
	@XStreamAlias("Code")
	private String code;
	/*名称*/
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
	

}
