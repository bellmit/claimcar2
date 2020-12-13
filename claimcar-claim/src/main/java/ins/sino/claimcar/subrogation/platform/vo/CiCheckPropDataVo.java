/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

//财产查勘情况列表(隶属于查勘信息)
/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiCheckPropDataVo {
	/** 损失财产名称 **/ 
	@XmlElement(name="PROTECT_NAME", required = true)
	private String protectName; 

	/** 损失描述 **/ 
	@XmlElement(name="LOSS_DESC")
	private String lossDesc; 

	/** 损失数量 **/ 
	@XmlElement(name="LOSS_NUM")
	private String lossNum; 

	/** 估损金额 **/ 
	@XmlElement(name="ESTIMATE_AMOUNT")
	private Double estimateAmount; 

	/** 财产属性 **/ 
	@XmlElement(name="PROTECT_PROPERTY")
	private String protectProperty; 

	/** 现场类别 **/ 
	@XmlElement(name="FIELD_TYPE")
	private String fieldType; 

	/** 查勘地点 **/ 
	@XmlElement(name="CHECK_ADDR")
	private String checkAddr; 

	/** 查勘情况说明 **/ 
	@XmlElement(name="CHECK_DES")
	private String checkDes;

	public String getProtectName() {
		return protectName;
	}

	public void setProtectName(String protectName) {
		this.protectName = protectName;
	}

	public String getLossDesc() {
		return lossDesc;
	}

	public void setLossDesc(String lossDesc) {
		this.lossDesc = lossDesc;
	}

	public String getLossNum() {
		return lossNum;
	}

	public void setLossNum(String lossNum) {
		this.lossNum = lossNum;
	}

	public Double getEstimateAmount() {
		return estimateAmount;
	}

	public void setEstimateAmount(Double estimateAmount) {
		this.estimateAmount = estimateAmount;
	}

	public String getProtectProperty() {
		return protectProperty;
	}

	public void setProtectProperty(String protectProperty) {
		this.protectProperty = protectProperty;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public String getCheckAddr() {
		return checkAddr;
	}

	public void setCheckAddr(String checkAddr) {
		this.checkAddr = checkAddr;
	}

	public String getCheckDes() {
		return checkDes;
	}

	public void setCheckDes(String checkDes) {
		this.checkDes = checkDes;
	} 

	
}
