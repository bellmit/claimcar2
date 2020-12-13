package ins.sino.claimcar.moblie.commonmark.caseDetails.vo;


import java.math.BigDecimal;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("REPAIRINFO")
public class DefLossRepairInfoVo {

	private static final long serialVersionUID = 8423652723600188374L;
	@XStreamAlias("REPAIRID")
	private String repairId;  //定损明细主键
	@XStreamAlias("REPAIRMODECODE")
	private String repairModeCode;  //修理方式代码
	@XStreamAlias("ITEMNAME")
	private String itemName;  //项目名称
	@XStreamAlias("MANPOWERFEE")
	private Double manPowerFee;  //定损人工费（工时定损费用）
	@XStreamAlias("MANPOWERREFFEE")
	private String manPowerrefFee;  //工时参考价格
	@XStreamAlias("SELFCONFIGFLAG")
	private String selfConfigFlag;  //自定义修理标记
	@XStreamAlias("ITEMCOVERCODE")
	private String itemCoverCode;  //险种代码
	@XStreamAlias("REMARK")
	private String remark;  //备注
	@XStreamAlias("EVALHOUR")
	private BigDecimal evalHour;  //工时数
	@XStreamAlias("REPAIRUNITPRICE")
	private BigDecimal repaiRunitPrice;  //工时单价
	@XStreamAlias("REPAIRLEVELCODE")
	private String repairLevelCode;  //损失程度代码
	@XStreamAlias("REPAIRLEVELNAME")
	private String repairLevelName;  //损失程度名称
	@XStreamAlias("LOSSFEE2")
	private Double lossFee2;  //核损修理费用
	@XStreamAlias("REMARK2")
	private String remark2;  //核损备注
	public String getRepairId() {
		return repairId;
	}
	public void setRepairId(String repairId) {
		this.repairId = repairId;
	}
	public String getRepairModeCode() {
		return repairModeCode;
	}
	public void setRepairModeCode(String repairModeCode) {
		this.repairModeCode = repairModeCode;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Double getManPowerFee() {
		return manPowerFee;
	}
	public void setManPowerFee(Double manPowerFee) {
		this.manPowerFee = manPowerFee;
	}
	public String getManPowerrefFee() {
		return manPowerrefFee;
	}
	public void setManPowerrefFee(String manPowerrefFee) {
		this.manPowerrefFee = manPowerrefFee;
	}
	public String getSelfConfigFlag() {
		return selfConfigFlag;
	}
	public void setSelfConfigFlag(String selfConfigFlag) {
		this.selfConfigFlag = selfConfigFlag;
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
	public BigDecimal getEvalHour() {
		return evalHour;
	}
	public void setEvalHour(BigDecimal evalHour) {
		this.evalHour = evalHour;
	}
	public BigDecimal getRepaiRunitPrice() {
		return repaiRunitPrice;
	}
	public void setRepaiRunitPrice(BigDecimal repaiRunitPrice) {
		this.repaiRunitPrice = repaiRunitPrice;
	}
	public String getRepairLevelCode() {
		return repairLevelCode;
	}
	public void setRepairLevelCode(String repairLevelCode) {
		this.repairLevelCode = repairLevelCode;
	}
	public String getRepairLevelName() {
		return repairLevelName;
	}
	public void setRepairLevelName(String repairLevelName) {
		this.repairLevelName = repairLevelName;
	}
	public Double getLossFee2() {
		return lossFee2;
	}
	public void setLossFee2(Double lossFee2) {
		this.lossFee2 = lossFee2;
	}
	public String getRemark2() {
		return remark2;
	}
	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}
	

	
}
