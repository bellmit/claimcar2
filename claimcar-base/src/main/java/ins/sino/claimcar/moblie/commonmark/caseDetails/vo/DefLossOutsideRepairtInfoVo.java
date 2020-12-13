package ins.sino.claimcar.moblie.commonmark.caseDetails.vo;


import java.math.BigDecimal;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("OUTSIDEREPAIRINFO")
public class DefLossOutsideRepairtInfoVo {

	private static final long serialVersionUID = 8423652723600188374L;
	@XStreamAlias("OUTERID")
	private String outerId;  //外修项目主键
	@XStreamAlias("OUTERNAME")
	private String outerName;  //外修项目名称
	@XStreamAlias("REFERENCEPRICE")
	private String referencePrice;  //参考工时费
	@XStreamAlias("REPAIRHANDADDFLAG")
	private String repairHandAddFlag;  //自定义标记
	@XStreamAlias("REPAIRLEVELCODE")
	private String repairLevelCode;  //修理程度
	@XStreamAlias("EVALOUTERPIRCE")
	private Double evalouterPirce;  //外修项目定损金额
	@XStreamAlias("DEROGATIONPRICE")
	private String derogationPrice;  //外修项目减损金额
	@XStreamAlias("DEROGATIONITEMNAME")
	private String derogationitemName;  //配件项目名称
	@XStreamAlias("DEROGATIONITEMCODE")
	private String derogationItemCode;  //配件零件号
	@XStreamAlias("DEROGATIONPRICETYPE")
	private String derogationPriceType;  //配件价格类型
	@XStreamAlias("PARTPRICE")
	private BigDecimal partPrice;  //配件金额
	@XStreamAlias("REPAIRFACTORYID")
	private String repairFactoryId;  //外修修理厂ID
	@XStreamAlias("REPAIRFACTORYNAME")
	private String repairFactoryName;  //外修修理厂名称
	@XStreamAlias("REPAIRFACTORYCODE")
	private String repairFactoryCode;  //外修修理厂代码
	@XStreamAlias("ITEMCOVERCODE")
	private String itemCoverCode;  //险种代码
	@XStreamAlias("REMARK")
	private String remark;  //备注
	@XStreamAlias("REPAIROUTERSUM")
	private BigDecimal repairouterSum;  //外修费用小计金额
	@XStreamAlias("REFERENCEPARTPRICE")
	private String referencePartPrice;  //外修配件参考价
	@XStreamAlias("PARTAMOUNT")
	private String partAmount;  //外修配件数量
	@XStreamAlias("OUTITEMAMOUNT")
	private BigDecimal outitemAmount;  //外修数量
	@XStreamAlias("LOSSFEE2")
	private BigDecimal lossFee2;//外修核损价格
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
	public String getRepairHandAddFlag() {
		return repairHandAddFlag;
	}
	public void setRepairHandAddFlag(String repairHandAddFlag) {
		this.repairHandAddFlag = repairHandAddFlag;
	}
	public String getRepairLevelCode() {
		return repairLevelCode;
	}
	public void setRepairLevelCode(String repairLevelCode) {
		this.repairLevelCode = repairLevelCode;
	}
	public Double getEvalouterPirce() {
		return evalouterPirce;
	}
	public void setEvalouterPirce(Double evalouterPirce) {
		this.evalouterPirce = evalouterPirce;
	}
	public String getDerogationPrice() {
		return derogationPrice;
	}
	public void setDerogationPrice(String derogationPrice) {
		this.derogationPrice = derogationPrice;
	}
	public String getDerogationitemName() {
		return derogationitemName;
	}
	public void setDerogationitemName(String derogationitemName) {
		this.derogationitemName = derogationitemName;
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
	public BigDecimal getPartPrice() {
		return partPrice;
	}
	public void setPartPrice(BigDecimal partPrice) {
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
	public BigDecimal getRepairouterSum() {
		return repairouterSum;
	}
	public void setRepairouterSum(BigDecimal repairouterSum) {
		this.repairouterSum = repairouterSum;
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
	public BigDecimal getOutitemAmount() {
		return outitemAmount;
	}
	public void setOutitemAmount(BigDecimal outitemAmount) {
		this.outitemAmount = outitemAmount;
	}
	public BigDecimal getLossFee2() {
		return lossFee2;
	}
	public void setLossFee2(BigDecimal lossFee2) {
		this.lossFee2 = lossFee2;
	}
	
	
}
