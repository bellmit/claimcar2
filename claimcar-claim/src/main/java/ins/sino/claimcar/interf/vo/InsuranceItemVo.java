package ins.sino.claimcar.interf.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("InsuranceItem")
public class InsuranceItemVo {
	/*调整比率*/
	@XStreamAlias("AdjustmentRatio")
	private String adjustmentRatio;
	/*免赔率*/
	@XStreamAlias("DeductibleRate")
	private String deductibleRate;
	/*折扣率*/
	@XStreamAlias("Discount")
	private String discount;
	/*保额/限额*/
	@XStreamAlias("InsuranceAmount")
	private String insuranceAmount;
	/*标准保费*/
	@XStreamAlias("InsuranceFee")
	private String insuranceFee;
	/*险种Code*/
	@XStreamAlias("InsuranceItemCode")
	private String insuranceItemCode;
	/*险种名称*/
	@XStreamAlias("Name")
	private String name;
	/*承包不计免赔*/
	@XStreamAlias("NonDeductible")
	private String nonDeductible;
	/*数量*/
	@XStreamAlias("Qty")
	private String qty;
	/*备注*/
	@XStreamAlias("Remark")
	private String remark;
	/*单次赔偿限额*/
	@XStreamAlias("SingleInsuranceAmount")
	private String singleInsuranceAmount;
	public String getAdjustmentRatio() {
		return adjustmentRatio;
	}
	public void setAdjustmentRatio(String adjustmentRatio) {
		this.adjustmentRatio = adjustmentRatio;
	}
	public String getDeductibleRate() {
		return deductibleRate;
	}
	public void setDeductibleRate(String deductibleRate) {
		this.deductibleRate = deductibleRate;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getInsuranceAmount() {
		return insuranceAmount;
	}
	public void setInsuranceAmount(String insuranceAmount) {
		this.insuranceAmount = insuranceAmount;
	}
	public String getInsuranceFee() {
		return insuranceFee;
	}
	public void setInsuranceFee(String insuranceFee) {
		this.insuranceFee = insuranceFee;
	}
	public String getInsuranceItemCode() {
		return insuranceItemCode;
	}
	public void setInsuranceItemCode(String insuranceItemCode) {
		this.insuranceItemCode = insuranceItemCode;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNonDeductible() {
		return nonDeductible;
	}
	public void setNonDeductible(String nonDeductible) {
		this.nonDeductible = nonDeductible;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSingleInsuranceAmount() {
		return singleInsuranceAmount;
	}
	public void setSingleInsuranceAmount(String singleInsuranceAmount) {
		this.singleInsuranceAmount = singleInsuranceAmount;
	}
	

}
