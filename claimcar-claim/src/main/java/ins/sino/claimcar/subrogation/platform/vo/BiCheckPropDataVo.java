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
public class BiCheckPropDataVo {
	@XmlElement(name = "ProtectName", required = true)
	private String protectName;//损失财产名称

	@XmlElement(name = "LossDesc")
	private String lossDesc;//损失描述

	@XmlElement(name = "LossNum")
	private String lossNum;//损失数量

	@XmlElement(name = "EstimatedLossAmount")
	private Double estimatedLossAmount;//估损金额

	@XmlElement(name = "ProtectProperty")
	private String protectProperty;//财产属性；参见代码

	@XmlElement(name = "FieldType")
	private String fieldType;//现场类别；参见代码

	@XmlElement(name = "CheckAddr")
	private String checkAddr;//查勘地点

	@XmlElement(name = "CheckDesc")
	private String checkDesc;//查勘情况说明


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
	 * @return 返回 lossNum  损失数量
	 */ 
	public String getLossNum(){ 
	    return lossNum;
	}

	/** 
	 * @param lossNum 要设置的 损失数量
	 */ 
	public void setLossNum(String lossNum){ 
	    this.lossNum=lossNum;
	}

	/** 
	 * @return 返回 estimatedLossAmount  估损金额
	 */ 
	public Double getEstimatedLossAmount(){ 
	    return estimatedLossAmount;
	}

	/** 
	 * @param estimatedLossAmount 要设置的 估损金额
	 */ 
	public void setEstimatedLossAmount(Double estimatedLossAmount){ 
	    this.estimatedLossAmount=estimatedLossAmount;
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

	/** 
	 * @return 返回 checkAddr  查勘地点
	 */ 
	public String getCheckAddr(){ 
	    return checkAddr;
	}

	/** 
	 * @param checkAddr 要设置的 查勘地点
	 */ 
	public void setCheckAddr(String checkAddr){ 
	    this.checkAddr=checkAddr;
	}

	/** 
	 * @return 返回 checkDesc  查勘情况说明
	 */ 
	public String getCheckDesc(){ 
	    return checkDesc;
	}

	/** 
	 * @param checkDesc 要设置的 查勘情况说明
	 */ 
	public void setCheckDesc(String checkDesc){ 
	    this.checkDesc=checkDesc;
	}




	
}
