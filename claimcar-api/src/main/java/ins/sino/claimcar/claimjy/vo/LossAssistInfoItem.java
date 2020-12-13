package ins.sino.claimcar.claimjy.vo;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class LossAssistInfoItem implements Serializable{
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "AssistId")
	private String assistId;//辅料明细主键
	@XmlElement(name = "ItemName")
	private String itemName;//辅料名称
	@XmlElement(name = "Count")
	private String count;//数量
	@XmlElement(name = "MaterialFee")
	private String materialFee;//外修项目定损金额
	@XmlElement(name = "EvalMateSum")
	private String evalMateSum;//辅料合计
	@XmlElement(name = "SelfConfigFlag")
	private String selfConfigFlag;//自定义辅料标记 是/否 1/0
	@XmlElement(name = "ItemCoverCode")
	private String itemCoverCode;//险种代码
	@XmlElement(name = "Remark")
	private String remark;//备注
	
	public String getAssistId() {
		return assistId;
	}
	public void setAssistId(String assistId) {
		this.assistId = assistId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getMaterialFee() {
		return materialFee;
	}
	public void setMaterialFee(String materialFee) {
		this.materialFee = materialFee;
	}
	public String getEvalMateSum() {
		return evalMateSum;
	}
	public void setEvalMateSum(String evalMateSum) {
		this.evalMateSum = evalMateSum;
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
}
