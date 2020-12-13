package ins.sino.claimcar.genilex.dlossReqVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("EvalVehicleLossPart")
public class EvalVehicleLossPartVo implements Serializable{
	private static final long serialVersionUID = 1L;
@XStreamAlias("EvalID")
private String evalID;//定核损ID
@XStreamAlias("LossPart")
private String lossPart;//车辆损失部位		
@XStreamAlias("ItemCode")
private String itemCode;//部位编号			
@XStreamAlias("ItemName")
private String itemName;//部位名称		
@XStreamAlias("ItemTypeCode")
private String itemTypeCode;//部位类型代码		
@XStreamAlias("ItemAmount")
private String itemAmount;//估损金额		
@XStreamAlias("CurrencyCode")
private String currencyCode;//币种	
@XStreamAlias("UnitPriceAmt")
private String unitPriceAmt;//配件价格	
@XStreamAlias("SystemUnitPrice")
private String systemUnitPrice;//系统参考价格		
@XStreamAlias("LocalUnitPrice")
private String localUnitPrice;//本地参考价格
@XStreamAlias("VerifiedUnitPriceAmt")
private String verifiedUnitPriceAmt;//核损单价	
@XStreamAlias("ManhourCost")
private String manhourCost;//工时费用		
@XStreamAlias("VerifiedManhourCost")
private String verifiedManhourCost;//核损金额		
@XStreamAlias("ManageFeeFactor")
private String manageFeeFactor;//管理费			
@XStreamAlias("OemPartCode")
private String oemPartCode;//原厂零件编号		
@XStreamAlias("ExaminPriceAmt")
private String examinPriceAmt;//核价单价	
@XStreamAlias("Remark")
private String remark;//备注			
@XStreamAlias("CreateBy")
private String createBy;//创建者		
@XStreamAlias("CreateTime")
private String createTime;//创建日期		
@XStreamAlias("UpdateBy")
private String updateBy;//更新者		
@XStreamAlias("UpdateTime")
private String updateTime;//更新日期		
public String getEvalID() {
	return evalID;
}
public void setEvalID(String evalID) {
	this.evalID = evalID;
}
public String getLossPart() {
	return lossPart;
}
public void setLossPart(String lossPart) {
	this.lossPart = lossPart;
}
public String getItemCode() {
	return itemCode;
}
public void setItemCode(String itemCode) {
	this.itemCode = itemCode;
}
public String getItemName() {
	return itemName;
}
public void setItemName(String itemName) {
	this.itemName = itemName;
}
public String getItemTypeCode() {
	return itemTypeCode;
}
public void setItemTypeCode(String itemTypeCode) {
	this.itemTypeCode = itemTypeCode;
}
public String getItemAmount() {
	return itemAmount;
}
public void setItemAmount(String itemAmount) {
	this.itemAmount = itemAmount;
}
public String getCurrencyCode() {
	return currencyCode;
}
public void setCurrencyCode(String currencyCode) {
	this.currencyCode = currencyCode;
}
public String getUnitPriceAmt() {
	return unitPriceAmt;
}
public void setUnitPriceAmt(String unitPriceAmt) {
	this.unitPriceAmt = unitPriceAmt;
}
public String getSystemUnitPrice() {
	return systemUnitPrice;
}
public void setSystemUnitPrice(String systemUnitPrice) {
	this.systemUnitPrice = systemUnitPrice;
}
public String getLocalUnitPrice() {
	return localUnitPrice;
}
public void setLocalUnitPrice(String localUnitPrice) {
	this.localUnitPrice = localUnitPrice;
}
public String getVerifiedUnitPriceAmt() {
	return verifiedUnitPriceAmt;
}
public void setVerifiedUnitPriceAmt(String verifiedUnitPriceAmt) {
	this.verifiedUnitPriceAmt = verifiedUnitPriceAmt;
}
public String getManhourCost() {
	return manhourCost;
}
public void setManhourCost(String manhourCost) {
	this.manhourCost = manhourCost;
}
public String getVerifiedManhourCost() {
	return verifiedManhourCost;
}
public void setVerifiedManhourCost(String verifiedManhourCost) {
	this.verifiedManhourCost = verifiedManhourCost;
}
public String getManageFeeFactor() {
	return manageFeeFactor;
}
public void setManageFeeFactor(String manageFeeFactor) {
	this.manageFeeFactor = manageFeeFactor;
}
public String getOemPartCode() {
	return oemPartCode;
}
public void setOemPartCode(String oemPartCode) {
	this.oemPartCode = oemPartCode;
}
public String getExaminPriceAmt() {
	return examinPriceAmt;
}
public void setExaminPriceAmt(String examinPriceAmt) {
	this.examinPriceAmt = examinPriceAmt;
}
public String getRemark() {
	return remark;
}
public void setRemark(String remark) {
	this.remark = remark;
}
public String getCreateBy() {
	return createBy;
}
public void setCreateBy(String createBy) {
	this.createBy = createBy;
}
public String getCreateTime() {
	return createTime;
}
public void setCreateTime(String createTime) {
	this.createTime = createTime;
}
public String getUpdateBy() {
	return updateBy;
}
public void setUpdateBy(String updateBy) {
	this.updateBy = updateBy;
}
public String getUpdateTime() {
	return updateTime;
}
public void setUpdateTime(String updateTime) {
	this.updateTime = updateTime;
}
		

}
