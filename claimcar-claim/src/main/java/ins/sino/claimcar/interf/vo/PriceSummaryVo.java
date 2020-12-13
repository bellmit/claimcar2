package ins.sino.claimcar.interf.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/*价格概述*/
@XStreamAlias("PriceSummary")
public class PriceSummaryVo {
	/*案件自带的定损金额*/
	@XStreamAlias("ConfirmLossPrice")
	private String confirmLossPrice;
	/*欺诈风险减损金额*/
	@XStreamAlias("FraudRiskSavingPrice")
	private String fraudRiskSavingPrice;
	/*配件总定损金额*/
	@XStreamAlias("PartTotalPrice")
	private String partTotalPrice;
	/*CE建议配件总金额*/
	@XStreamAlias("CEPartTotalPrice")
	private String cEPartTotalPrice;
	/*建议配件减损总金额*/
	@XStreamAlias("SavingPartTotalPrice")
	private String savingPartTotalPrice;
	/*工时总定损金额*/
	@XStreamAlias("LaborTotalPrice")
	private String laborTotalPrice;
	/*CE建议工时总金额*/
	@XStreamAlias("CELaborTotalPrice")
	private String cELaborTotalPrice;
	/*建议工时减损总金额*/
	@XStreamAlias("SavingLaborTotalPrice")
	private String savingLaborTotalPrice;
	/*定损金额总计：配件总定损金额+工时总定损金额*/
	@XStreamAlias("TotalPrice")
	private String totalPrice;
	/*CE建议金额总计：CE建议配件总金额+CE建议工时总金额-欺诈风险减损金额*/
	@XStreamAlias("CETotalPrice")
	private String cETotalPrice;
	/*建议减损金额总计：建议配件减损总金额+建议工时减损总金额+欺诈风险减损金额*/
	@XStreamAlias("SavingTotalPrice")
	private String savingTotalPrice;
	public String getConfirmLossPrice() {
		return confirmLossPrice;
	}
	public void setConfirmLossPrice(String confirmLossPrice) {
		this.confirmLossPrice = confirmLossPrice;
	}
	public String getFraudRiskSavingPrice() {
		return fraudRiskSavingPrice;
	}
	public void setFraudRiskSavingPrice(String fraudRiskSavingPrice) {
		this.fraudRiskSavingPrice = fraudRiskSavingPrice;
	}
	public String getPartTotalPrice() {
		return partTotalPrice;
	}
	public void setPartTotalPrice(String partTotalPrice) {
		this.partTotalPrice = partTotalPrice;
	}
	public String getcEPartTotalPrice() {
		return cEPartTotalPrice;
	}
	public void setcEPartTotalPrice(String cEPartTotalPrice) {
		this.cEPartTotalPrice = cEPartTotalPrice;
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
	public String getcELaborTotalPrice() {
		return cELaborTotalPrice;
	}
	public void setcELaborTotalPrice(String cELaborTotalPrice) {
		this.cELaborTotalPrice = cELaborTotalPrice;
	}
	public String getSavingLaborTotalPrice() {
		return savingLaborTotalPrice;
	}
	public void setSavingLaborTotalPrice(String savingLaborTotalPrice) {
		this.savingLaborTotalPrice = savingLaborTotalPrice;
	}
	public String getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getcETotalPrice() {
		return cETotalPrice;
	}
	public void setcETotalPrice(String cETotalPrice) {
		this.cETotalPrice = cETotalPrice;
	}
	public String getSavingTotalPrice() {
		return savingTotalPrice;
	}
	public void setSavingTotalPrice(String savingTotalPrice) {
		this.savingTotalPrice = savingTotalPrice;
	}
	
}
