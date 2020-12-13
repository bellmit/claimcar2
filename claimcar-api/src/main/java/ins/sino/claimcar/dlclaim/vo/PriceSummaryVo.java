package ins.sino.claimcar.dlclaim.vo;

import java.io.Serializable;

public class PriceSummaryVo implements Serializable{
private static final long serialVersionUID = 1L;
private String confirmLossPrice;//保险公司提交金额
private String fraudRiskHit;//欺诈风险命中数量
private String partTotalPrice;//配件总金额
private String cePartTotalPrice;//CE建议配件总金额
private String savingPartTotalPrice;//CE建议配件减损总金额
private String laborTotalPrice;//工时总金额
private String ceLaborTotalPrice;//CE建议工时总金额
private String savingLaborTotalPrice;//CE建议工时减损总金额
private String smallPartTotalPrice;//辅料总金额
private String ceSmallPartTotalPrice;//CE建议辅料总金额
private String savingSmallPartTotalPrice;//CE建议辅料减损总金额
private String totalPrice;//金额总计
private String ceTotalPrice;//CE建议金额总计
private String savingTotalPrice;//CE建议减损金额总计
private String rescueFee;//保险公司提交施救费
private String ceRescueFee;//CE建议施救费
private String savingRescueFee;//CE建议施救费减损金额
public String getConfirmLossPrice() {
	return confirmLossPrice;
}
public void setConfirmLossPrice(String confirmLossPrice) {
	this.confirmLossPrice = confirmLossPrice;
}
public String getFraudRiskHit() {
	return fraudRiskHit;
}
public void setFraudRiskHit(String fraudRiskHit) {
	this.fraudRiskHit = fraudRiskHit;
}
public String getPartTotalPrice() {
	return partTotalPrice;
}
public void setPartTotalPrice(String partTotalPrice) {
	this.partTotalPrice = partTotalPrice;
}
public String getCePartTotalPrice() {
	return cePartTotalPrice;
}
public void setCePartTotalPrice(String cePartTotalPrice) {
	this.cePartTotalPrice = cePartTotalPrice;
}
public String getSavingPartTotalPrice() {
	return savingPartTotalPrice;
}
public void setSavingPartTotalPrice(String savingPartTotalPrice) {
	this.savingPartTotalPrice = savingPartTotalPrice;
}
public String getLaborTotalPrice() {
	return laborTotalPrice;
}
public void setLaborTotalPrice(String laborTotalPrice) {
	this.laborTotalPrice = laborTotalPrice;
}
public String getCeLaborTotalPrice() {
	return ceLaborTotalPrice;
}
public void setCeLaborTotalPrice(String ceLaborTotalPrice) {
	this.ceLaborTotalPrice = ceLaborTotalPrice;
}
public String getSavingLaborTotalPrice() {
	return savingLaborTotalPrice;
}
public void setSavingLaborTotalPrice(String savingLaborTotalPrice) {
	this.savingLaborTotalPrice = savingLaborTotalPrice;
}
public String getSmallPartTotalPrice() {
	return smallPartTotalPrice;
}
public void setSmallPartTotalPrice(String smallPartTotalPrice) {
	this.smallPartTotalPrice = smallPartTotalPrice;
}
public String getCeSmallPartTotalPrice() {
	return ceSmallPartTotalPrice;
}
public void setCeSmallPartTotalPrice(String ceSmallPartTotalPrice) {
	this.ceSmallPartTotalPrice = ceSmallPartTotalPrice;
}
public String getSavingSmallPartTotalPrice() {
	return savingSmallPartTotalPrice;
}
public void setSavingSmallPartTotalPrice(String savingSmallPartTotalPrice) {
	this.savingSmallPartTotalPrice = savingSmallPartTotalPrice;
}
public String getTotalPrice() {
	return totalPrice;
}
public void setTotalPrice(String totalPrice) {
	this.totalPrice = totalPrice;
}
public String getCeTotalPrice() {
	return ceTotalPrice;
}
public void setCeTotalPrice(String ceTotalPrice) {
	this.ceTotalPrice = ceTotalPrice;
}
public String getSavingTotalPrice() {
	return savingTotalPrice;
}
public void setSavingTotalPrice(String savingTotalPrice) {
	this.savingTotalPrice = savingTotalPrice;
}
public String getRescueFee() {
	return rescueFee;
}
public void setRescueFee(String rescueFee) {
	this.rescueFee = rescueFee;
}
public String getCeRescueFee() {
	return ceRescueFee;
}
public void setCeRescueFee(String ceRescueFee) {
	this.ceRescueFee = ceRescueFee;
}
public String getSavingRescueFee() {
	return savingRescueFee;
}
public void setSavingRescueFee(String savingRescueFee) {
	this.savingRescueFee = savingRescueFee;
}


}
