/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

//人员查勘情况列表(隶属于查勘信息)
/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BiCheckPersonDataVo {
	@XmlElement(name = "PersonPayType", required = true)
	private String personPayType;//伤亡类型；参见代码

	@XmlElement(name = "EstimatedLossAmount")
	private Double estimatedLossAmount;//估损金额


	/** 
	 * @return 返回 personPayType  伤亡类型；参见代码
	 */ 
	public String getPersonPayType(){ 
	    return personPayType;
	}

	/** 
	 * @param personPayType 要设置的 伤亡类型；参见代码
	 */ 
	public void setPersonPayType(String personPayType){ 
	    this.personPayType=personPayType;
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




	
}
