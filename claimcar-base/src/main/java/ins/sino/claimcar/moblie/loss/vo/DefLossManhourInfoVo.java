package ins.sino.claimcar.moblie.loss.vo;


import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("MANHOURINFO")
public class DefLossManhourInfoVo implements Serializable{
	private static final long serialVersionUID = 8423652723600188374L;
	@XStreamAlias("REPAIRID")
	private String repairId;//定损修理信息主键 要保存，这是定损系统的明细主键，理赔系统可能有自己的明细主键，靠这个对应两边的明细
	@XStreamAlias("REPAIRMODECODE")
	private String repairModeCode;//修理方式代码 1-喷漆项目 2-钣金项目 3-电工项目 4-机修项目 5-拆装项目
	@XStreamAlias("ITEMNAME")
	private String itemName;//项目名称
	@XStreamAlias("MANPOWERFEE")
	private String manpowerFee;//定损人工费
	@XStreamAlias("MANPOWERREFFEE")
	private String manpowerRefFee;//工时参考价格
	@XStreamAlias("SELFCONFIGFLAG")
	private String selfConfigFlag;//自定义修理标记
	@XStreamAlias("ITEMCOVERCODE")
	private String itemCoverCode;//险种代码
	@XStreamAlias("REMARK")
	private String remark;//备注
	@XStreamAlias("EVALHOUR")
	private String evalHour;//工时数
	@XStreamAlias("REPAIRUNITPRICE")
	private String repairUnitPrice;//工时单价
	@XStreamAlias("REPAIRLEVELCODE")
	private String repairLevelCode;//损失程度代码
	@XStreamAlias("REPAIRLEVELNAME")
	private String repairLevelName;//损失程度名称
	@XStreamAlias("LOSSFEE2")
	private String lossFee2;//核损价格
	@XStreamAlias("REMARK2")
	private String remark2;//核损备注
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
	public String getManpowerFee() {
		return manpowerFee;
	}
	public void setManpowerFee(String manpowerFee) {
		this.manpowerFee = manpowerFee;
	}
	public String getManpowerRefFee() {
		return manpowerRefFee;
	}
	public void setManpowerRefFee(String manpowerRefFee) {
		this.manpowerRefFee = manpowerRefFee;
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
	public String getEvalHour() {
		return evalHour;
	}
	public void setEvalHour(String evalHour) {
		this.evalHour = evalHour;
	}
	public String getRepairUnitPrice() {
		return repairUnitPrice;
	}
	public void setRepairUnitPrice(String repairUnitPrice) {
		this.repairUnitPrice = repairUnitPrice;
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
	public String getLossFee2() {
		return lossFee2;
	}
	public void setLossFee2(String lossFee2) {
		this.lossFee2 = lossFee2;
	}
	public String getRemark2() {
		return remark2;
	}
	public void setRemark2(String remark2) {
		this.remark2 = remark2;
	}
}
