package ins.sino.claimcar.claimcarYJ.vo;

public class SupplyYjVo implements java.io.Serializable{
private String partcode;//零部件编码
private String partName;//零部件名称
private String dlossPrice;//定损单价
private String dlossNums;//定损数量
private String dlossRestFee;//定损残值
private String recycleFlag;//是否回收
private String yjPrice;//阳杰报价
private String status;//下单状态
private String originalId;//原厂编码
private String quotationAmount;//供货协商单价
private String thirdpartenquiryid;//配件Id
private String priceType;//价格类型
public String getPartcode() {
	return partcode;
}
public void setPartcode(String partcode) {
	this.partcode = partcode;
}
public String getDlossPrice() {
	return dlossPrice;
}
public void setDlossPrice(String dlossPrice) {
	this.dlossPrice = dlossPrice;
}
public String getDlossNums() {
	return dlossNums;
}
public void setDlossNums(String dlossNums) {
	this.dlossNums = dlossNums;
}
public String getDlossRestFee() {
	return dlossRestFee;
}
public void setDlossRestFee(String dlossRestFee) {
	this.dlossRestFee = dlossRestFee;
}
public String getRecycleFlag() {
	return recycleFlag;
}
public void setRecycleFlag(String recycleFlag) {
	this.recycleFlag = recycleFlag;
}
public String getYjPrice() {
	return yjPrice;
}
public void setYjPrice(String yjPrice) {
	this.yjPrice = yjPrice;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public String getOriginalId() {
	return originalId;
}
public void setOriginalId(String originalId) {
	this.originalId = originalId;
}
public String getQuotationAmount() {
	return quotationAmount;
}
public void setQuotationAmount(String quotationAmount) {
	this.quotationAmount = quotationAmount;
}
public String getThirdpartenquiryid() {
	return thirdpartenquiryid;
}
public void setThirdpartenquiryid(String thirdpartenquiryid) {
	this.thirdpartenquiryid = thirdpartenquiryid;
}
public String getPartName() {
	return partName;
}
public void setPartName(String partName) {
	this.partName = partName;
}
public String getPriceType() {
	return priceType;
}
public void setPriceType(String priceType) {
	this.priceType = priceType;
}


}
