package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class LossRepairInfoItem implements Serializable{
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "RepairId")
	private String repairId;//定损修理信息主键 要保存，这是定损系统的明细主键，理赔系统可能有自己的明细主键，靠这个对应两边的明细
	@XmlElement(name = "RepairModeCode")
	private String repairModeCode;//修理方式代码 1-喷漆项目 2-钣金项目 3-电工项目 4-机修项目 5-拆装项目
	@XmlElement(name = "ItemName")
	private String itemName;//项目名称
	@XmlElement(name = "ManpowerFee")
	private String manpowerFee;//定损人工费
	@XmlElement(name = "ManpowerRefFee")
	private String manpowerRefFee;//工时参考价格
	@XmlElement(name = "SelfConfigFlag")
	private String selfConfigFlag;//自定义修理标记
	@XmlElement(name = "ItemCoverCode")
	private String itemCoverCode;//险种代码
	@XmlElement(name = "Remark")
	private String remark;//备注
	@XmlElement(name = "EvalHour")
	private String evalHour;//工时数
	@XmlElement(name = "RepairUnitPrice")
	private String repairUnitPrice;//工时单价
	@XmlElement(name = "RepairLevelCode")
	private String repairLevelCode;//损失程度代码
	@XmlElement(name = "RepairLevelName")
	private String repairLevelName;//损失程度名称
	
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
}
