/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

//人员伤亡费用明细列表（隶属于人员伤亡情况）
/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiPersonLossFeeDataVo {
	/** 费用类型；参见代码 **/ 
	@XmlElement(name="FEE_TYPE", required = true)
	private String feeType; 

	/** 核损金额 **/ 
	@XmlElement(name="UNDER_DEF_LOSS", required = true)
	private Double underDefLoss;

	public String getFeeType() {
		return feeType;
	}

	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}

	public Double getUnderDefLoss() {
		return underDefLoss;
	}

	public void setUnderDefLoss(Double underDefLoss) {
		this.underDefLoss = underDefLoss;
	} 



	
	
}
