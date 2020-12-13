/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

//财产损失情况列表(隶属于定核损信息)
/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiEstimatePropDataVo {
	/** 损失财产名称 **/ 
	@XmlElement(name="PROTECT_NAME", required = true)
	private String protectName; 

	/** 损失描述 **/ 
	@XmlElement(name="LOSS_DESC")
	private String lossDesc; 

	/** 核损金额 **/ 
	@XmlElement(name="UNDER_DEF_LOSS", required = true)
	private Double underDefLoss; 

	/** 财产属性(本车财产/外车财产)；参见代码 **/ 
	@XmlElement(name="PROTECT_PROPERTY", required = true)
	private String protectProperty; 

	/** 现场类别；参见代码 **/ 
	@XmlElement(name="FILED_TYPE", required = true)
	private String filedType;

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

	public Double getUnderDefLoss() {
		return underDefLoss;
	}

	public void setUnderDefLoss(Double underDefLoss) {
		this.underDefLoss = underDefLoss;
	}

	public String getProtectProperty() {
		return protectProperty;
	}

	public void setProtectProperty(String protectProperty) {
		this.protectProperty = protectProperty;
	}

	public String getFiledType() {
		return filedType;
	}

	public void setFiledType(String filedType) {
		this.filedType = filedType;
	} 


	
	
}
