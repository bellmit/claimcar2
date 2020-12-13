package ins.sino.claimcar.claimjy.vo.vlossreturn;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class JyVLossReturnOutRepairInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "OuterId")
	private String outerId;

	@XmlElement(name = "OuterName")
	private String outerName;

	@XmlElement(name = "ReferencePrice")
	private String referencePrice;

	@XmlElement(name = "RepairHandaddFlag")
	private String repairHandaddFlag;

	@XmlElement(name = "EvalOuterPirce")
	private String evalOuterPirce;

	@XmlElement(name = "DerogationPrice")
	private String derogationPrice;

	@XmlElement(name = "DerogationItemName")
	private String derogationItemName;

	@XmlElement(name = "DerogationItemCode")
	private String derogationItemCode;

	@XmlElement(name = "DerogationPriceType")
	private String derogationPriceType;

	@XmlElement(name = "PartPrice")
	private String partPrice;

	@XmlElement(name = "RepairFactoryId")
	private String repairFactoryId;

	@XmlElement(name = "RepairFactoryName")
	private String repairFactoryName;

	@XmlElement(name = "RepairFactoryCode")
	private String repairFactoryCode;

	@XmlElement(name = "ItemCoverCode")
	private String itemCoverCode;

	@XmlElement(name = "CheckState")
	private String checkState;

	@XmlElement(name = "Remark")
	private String remark;

	@XmlElement(name = "PartAmount")
	private String partAmount;

	@XmlElement(name = "RepairOuterSum")
	private String repairOuterSum;

	@XmlElement(name = "ReferencePartPrice")
	private String referencePartPrice;

	@XmlElement(name = "OutItemAmount")
	private String outItemAmount;

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

	public String getCheckState() {
		return checkState;
	}

	public void setCheckState(String checkState) {
		this.checkState = checkState;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPartAmount() {
		return partAmount;
	}

	public void setPartAmount(String partAmount) {
		this.partAmount = partAmount;
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

	public String getOutItemAmount() {
		return outItemAmount;
	}

	public void setOutItemAmount(String outItemAmount) {
		this.outItemAmount = outItemAmount;
	}

	

}
