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
public class BiPersonLossFeeDataVo {
	@XmlElement(name = "FeeType", required = true)
	private String feeType;//损失赔偿类型明细代码；参见代码

	@XmlElement(name = "UnderDefLoss", required = true)
	private Double underDefLoss;//核损金额


	/** 
	 * @return 返回 feeType  损失赔偿类型明细代码；参见代码
	 */ 
	public String getFeeType(){ 
	    return feeType;
	}

	/** 
	 * @param feeType 要设置的 损失赔偿类型明细代码；参见代码
	 */ 
	public void setFeeType(String feeType){ 
	    this.feeType=feeType;
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



	
	
}
