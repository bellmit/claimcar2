/******************************************************************************
* CREATETIME : 2016年3月17日 下午7:39:34
******************************************************************************/
package ins.sino.claimcar.subrogation.vo;

import java.io.Serializable;


// 财产损失情况列表(隶属于定核损信息)
/**
 * @author ★YangKun
 * @CreateTime 2016年3月17日
 */
public class EstimatePropDataVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/** 损失财产名称 **/ 
	private String protectName; 

	/** 损失描述 **/ 
	private String lossDesc; 

	/** 核损金额 **/ 
	private Double underDefLoss; 

	/** 财产属性(本车财产/外车财产)；参见代码 **/ 
	private String protectProperty; 

	/** 现场类别；参见代码 **/ 
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
