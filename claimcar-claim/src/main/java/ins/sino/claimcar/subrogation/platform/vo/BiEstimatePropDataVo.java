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
public class BiEstimatePropDataVo {
	@XmlElement(name = "ProtectName", required = true)
	private String protectName;//损失财产名称

	@XmlElement(name = "LossDesc")
	private String lossDesc;//损失描述

	@XmlElement(name = "UnderDefLoss", required = true)
	private Double underDefLoss;//核损金额

	@XmlElement(name = "ProtectProperty", required = true)
	private String protectProperty;//财产属性；参见代码

	@XmlElement(name = "FieldType", required = true)
	private String fieldType;//现场类别；参见代码


	/** 
	 * @return 返回 protectName  损失财产名称
	 */ 
	public String getProtectName(){ 
	    return protectName;
	}

	/** 
	 * @param protectName 要设置的 损失财产名称
	 */ 
	public void setProtectName(String protectName){ 
	    this.protectName=protectName;
	}

	/** 
	 * @return 返回 lossDesc  损失描述
	 */ 
	public String getLossDesc(){ 
	    return lossDesc;
	}

	/** 
	 * @param lossDesc 要设置的 损失描述
	 */ 
	public void setLossDesc(String lossDesc){ 
	    this.lossDesc=lossDesc;
	}

	/** 
	 * @return 返回 underDefLoss  核损金额
	 */ 
	public Double getUnderDefLoss(){ 
	    return underDefLoss;
	}

	/** 
	 * @param underDefLoss 要设置的 核损金额
	 */ 
	public void setUnderDefLoss(Double underDefLoss){ 
	    this.underDefLoss=underDefLoss;
	}

	/** 
	 * @return 返回 protectProperty  财产属性；参见代码
	 */ 
	public String getProtectProperty(){ 
	    return protectProperty;
	}

	/** 
	 * @param protectProperty 要设置的 财产属性；参见代码
	 */ 
	public void setProtectProperty(String protectProperty){ 
	    this.protectProperty=protectProperty;
	}

	/** 
	 * @return 返回 fieldType  现场类别；参见代码
	 */ 
	public String getFieldType(){ 
	    return fieldType;
	}

	/** 
	 * @param fieldType 要设置的 现场类别；参见代码
	 */ 
	public void setFieldType(String fieldType){ 
	    this.fieldType=fieldType;
	}



	
}
