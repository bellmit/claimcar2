package ins.sino.claimcar.moblie.loss.vo;


import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("OUTSIDEREPAIRINFO")
public class DefLossOutRepairInfoVo implements Serializable{
	private static final long serialVersionUID = 8423652723600188374L;
	@XStreamAlias("OUTERID")
	private String outerId;//外修项目主键 
	@XStreamAlias("OUTERNAME")
	private String outerName;//外修项目名称
	@XStreamAlias("REFERENCEPRICE")
	private String referencePrice;//参考工时费
	@XStreamAlias("REPAIRHANDADDFLAG")
	private String repairHandaddFlag;//自定义标记 是/否 1/0
	@XStreamAlias("REPAIRLEVELCODE")
	private String repairLevelCode;//修理程度
	@XStreamAlias("EVALOUTERPIRCE")
	private String evalOuterPirce;//外修项目定损金额
	@XStreamAlias("DEROGATIONPRICE")
	private String derogationPrice;//外修项目减损金额
	@XStreamAlias("DEROGATIONITEMNAME")
	private String derogationItemName;//配件项目名称 外修项目减损关联配件项目名称
	@XStreamAlias("DEROGATIONITEMCODE")
	private String derogationItemCode;//配件零件号 外修项目减损关联配件零件号
	@XStreamAlias("DEROGATIONPRICETYPE")
	private String derogationPriceType;//配件价格类型 1-4S店价 2-市场原厂价 3-品牌价 4-适用价 5-再制造价
	@XStreamAlias("PARTPRICE")
	private String partPrice;//配件金额
	@XStreamAlias("REPAIRFACTORYID")
	private String repairFactoryId;//外修修理厂ID 外修修理厂ID
	@XStreamAlias("REPAIRFACTORYNAME")
	private String repairFactoryName;//外修修理厂名称 外修修理厂名称
	@XStreamAlias("REPAIRFACTORYCODE")
	private String repairFactoryCode;//外修修理厂代码 外修修理厂代码
	@XStreamAlias("ITEMCOVERCODE")
	private String itemCoverCode;//险种代码
	@XStreamAlias("REMARK")
	private String remark;//备注
	@XStreamAlias("REPAIROUTERSUM")
	private String repairOuterSum;//外修费用小计金额 外修费用小计金额
	@XStreamAlias("REFERENCEPARTPRICE")
	private String referencePartPrice;//外修配件参考价 外修配件参考价
	@XStreamAlias("PARTAMOUNT")
	private String partAmount;//外修配件数量
	@XStreamAlias("OUTITEMAMOUNT")
	private String outItemAmount;//外修数量
	public String getOuterId() {
		return outerId;
	}
	public void setOuterId(String outerId) {
		this.outerId = outerId;
	}
	public String getOuterName() {
		return outerName;
	}
	public void setOuterName(String outerName) {
		this.outerName = outerName;
	}
	public String getReferencePrice() {
		return referencePrice;
	}
	public void setReferencePrice(String referencePrice) {
		this.referencePrice = referencePrice;
	}
	public String getRepairHandaddFlag() {
		return repairHandaddFlag;
	}
	public void setRepairHandaddFlag(String repairHandaddFlag) {
		this.repairHandaddFlag = repairHandaddFlag;
	}
	public String getRepairLevelCode() {
		return repairLevelCode;
	}
	public void setRepairLevelCode(String repairLevelCode) {
		this.repairLevelCode = repairLevelCode;
	}
	public String getEvalOuterPirce() {
		return evalOuterPirce;
	}
	public void setEvalOuterPirce(String evalOuterPirce) {
		this.evalOuterPirce = evalOuterPirce;
	}
	public String getDerogationPrice() {
		return derogationPrice;
	}
	public void setDerogationPrice(String derogationPrice) {
		this.derogationPrice = derogationPrice;
	}
	public String getDerogationItemName() {
		return derogationItemName;
	}
	public void setDerogationItemName(String derogationItemName) {
		this.derogationItemName = derogationItemName;
	}
	public String getDerogationItemCode() {
		return derogationItemCode;
	}
	public void setDerogationItemCode(String derogationItemCode) {
		this.derogationItemCode = derogationItemCode;
	}
	public String getDerogationPriceType() {
		return derogationPriceType;
	}
	public void setDerogationPriceType(String derogationPriceType) {
		this.derogationPriceType = derogationPriceType;
	}
	public String getPartPrice() {
		return partPrice;
	}
	public void setPartPrice(String partPrice) {
		this.partPrice = partPrice;
	}
	public String getRepairFactoryId() {
		return repairFactoryId;
	}
	public void setRepairFactoryId(String repairFactoryId) {
		this.repairFactoryId = repairFactoryId;
	}
	public String getRepairFactoryName() {
		return repairFactoryName;
	}
	public void setRepairFactoryName(String repairFactoryName) {
		this.repairFactoryName = repairFactoryName;
	}
	public String getRepairFactoryCode() {
		return repairFactoryCode;
	}
	public void setRepairFactoryCode(String repairFactoryCode) {
		this.repairFactoryCode = repairFactoryCode;
	}
	public String getItemCoverCode() {
		return itemCoverCode;
	}
	public void setItemCoverCode(String itemCoverCode) {
		this.itemCoverCode = itemCoverCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRepairOuterSum() {
		return repairOuterSum;
	}
	public void setRepairOuterSum(String repairOuterSum) {
		this.repairOuterSum = repairOuterSum;
	}
	public String getReferencePartPrice() {
		return referencePartPrice;
	}
	public void setReferencePartPrice(String referencePartPrice) {
		this.referencePartPrice = referencePartPrice;
	}
	public String getPartAmount() {
		return partAmount;
	}
	public void setPartAmount(String partAmount) {
		this.partAmount = partAmount;
	}
	public String getOutItemAmount() {
		return outItemAmount;
	}
	public void setOutItemAmount(String outItemAmount) {
		this.outItemAmount = outItemAmount;
	}
}
