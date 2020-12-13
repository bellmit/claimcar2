package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class LossOuterRepairInfoItem implements Serializable{
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "OuterId")
	private String outerId;//外修项目主键 
	@XmlElement(name = "OuterName")
	private String outerName;//外修项目名称
	@XmlElement(name = "RepairHandaddFlag")
	private String repairHandaddFlag;//自定义标记 是/否 1/0
	@XmlElement(name = "EvalOuterPirce")
	private String evalOuterPirce;//外修项目定损金额
	@XmlElement(name = "DerogationPrice")
	private String derogationPrice;//外修项目减损金额
	@XmlElement(name = "DerogationItemName")
	private String derogationItemName;//配件项目名称 外修项目减损关联配件项目名称
	@XmlElement(name = "DerogationItemCode")
	private String derogationItemCode;//配件零件号 外修项目减损关联配件零件号
	@XmlElement(name = "DerogationPriceType")
	private String derogationPriceType;//配件价格类型 1-4S店价 2-市场原厂价 3-品牌价 4-适用价 5-再制造价
	@XmlElement(name = "PartPrice")
	private String partPrice;//配件金额
	@XmlElement(name = "RepairFactoryId")
	private String repairFactoryId;//外修修理厂ID 外修修理厂ID
	@XmlElement(name = "RepairFactoryName")
	private String repairFactoryName;//外修修理厂名称 外修修理厂名称
	@XmlElement(name = "RepairFactoryCode")
	private String repairFactoryCode;//外修修理厂代码 外修修理厂代码
	@XmlElement(name = "ItemCoverCode")
	private String itemCoverCode;//险种代码
	@XmlElement(name = "Remark")
	private String remark;//备注
	@XmlElement(name = "PartAmount")
	private int partAmount;//外修配件数量
	@XmlElement(name = "OutItemAmount")
	private String outItemAmount;//外修数量
	@XmlElement(name = "RepairOuterSum")
	private String repairOuterSum;//外修费用小计金额 外修费用小计金额
	@XmlElement(name = "ReferencePartPrice")
	private String referencePartPrice;//外修配件参考价 外修配件参考价
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
	public String getRepairHandaddFlag() {
		return repairHandaddFlag;
	}
	public void setRepairHandaddFlag(String repairHandaddFlag) {
		this.repairHandaddFlag = repairHandaddFlag;
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
	public int getPartAmount() {
		return partAmount;
	}
	public void setPartAmount(int partAmount) {
		this.partAmount = partAmount;
	}
	public String getOutItemAmount() {
		return outItemAmount;
	}
	public void setOutItemAmount(String outItemAmount) {
		this.outItemAmount = outItemAmount;
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
}
