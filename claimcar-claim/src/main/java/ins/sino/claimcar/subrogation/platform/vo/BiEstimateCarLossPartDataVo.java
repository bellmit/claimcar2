/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

//车辆损失部位列表（隶属于车辆损失情况）
/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BiEstimateCarLossPartDataVo {
	@XmlElement(name = "LossPart")
	private String lossPart;//车辆损失部位


	/** 
	 * @return 返回 lossPart  车辆损失部位
	 */ 
	public String getLossPart(){ 
	    return lossPart;
	}

	/** 
	 * @param lossPart 要设置的 车辆损失部位
	 */ 
	public void setLossPart(String lossPart){ 
	    this.lossPart=lossPart;
	}




	
	
}
