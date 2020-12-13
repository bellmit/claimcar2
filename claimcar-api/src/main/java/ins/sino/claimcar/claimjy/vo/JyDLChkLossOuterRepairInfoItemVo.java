package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * <pre></pre>
 * @author ★LiYi
 */
@XStreamAlias("Item")
public class JyDLChkLossOuterRepairInfoItemVo implements Serializable {

	@XStreamAlias("OuterId")
	private String outerId;

	@XStreamAlias("OuterName")
	private String outerName;

	@XStreamAlias("ReferencePrice")
	private String referencePrice;

	@XStreamAlias("RepairHandaddFlag")
	private String repairHandaddFlag;

	@XStreamAlias("EvalOuterPirce")
	private String evalOuterPirce;

	@XStreamAlias("DerogationPrice")
	private String derogationPrice;

	@XStreamAlias("DerogationItemName")
	private String derogationItemName;

	@XStreamAlias("DerogationItemCode")
	private String derogationItemCode;

	@XStreamAlias("DerogationPriceType")
	private String derogationPriceType;

	@XStreamAlias("PartPrice")
	private String partPrice;

	@XStreamAlias("RepairFactoryId")
	private String repairFactoryId;

	@XStreamAlias("RepairFactoryName")
	private String repairFactoryName;

	@XStreamAlias("RepairFactoryCode")
	private String repairFactoryCode;

	@XStreamAlias("ItemCoverCode")
	private String itemCoverCode;

	@XStreamAlias("CheckState")
	private String checkState;

	@XStreamAlias("Remark")
	private String remark;

	@XStreamAlias("PartAmount")
	private String partAmount;

	@XStreamAlias("RepairOuterSum")
	private String repairOuterSum;

	@XStreamAlias("ReferencePartPrice")
	private String referencePartPrice;

	@XStreamAlias("OutItemAmount")
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

	public JyDLChkLossOuterRepairInfoItemVo(){
		super();
	}

}
