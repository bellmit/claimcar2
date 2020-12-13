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
public class CiCheckPersonDataVo {
	/** 伤亡类型 **/ 
	@XmlElement(name="PERSON_PAY_TYPE", required = true)
	private String personPayType; 

	/** 估损金额 **/ 
	@XmlElement(name="ESTIMATE_AMOUNT")
	private Double estimateAmount;

	public String getPersonPayType() {
		return personPayType;
	}

	public void setPersonPayType(String personPayType) {
		this.personPayType = personPayType;
	}

	public Double getEstimateAmount() {
		return estimateAmount;
	}

	public void setEstimateAmount(Double estimateAmount) {
		this.estimateAmount = estimateAmount;
	} 

	
}
