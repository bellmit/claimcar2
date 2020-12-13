package ins.sino.claimcar.claimjy.vo.pricereturn;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class JyPriceReturnReqAssistInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@XmlElement(name = "AssistId")
	private String assistId;

	@XmlElement(name = "ItemName")
	private String itemName;

	@XmlElement(name = "Count")
	private String count;

	@XmlElement(name = "MaterialFee")
	private String materialFee;

	@XmlElement(name = "EvalMateSum")
	private String evalMateSum;

	@XmlElement(name = "SelfConfigFlag")
	private String selfConfigFlag;

	@XmlElement(name = "ItemCoverCode")
	private String itemCoverCode;

	@XmlElement(name = "Remark")
	private String remark;

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
