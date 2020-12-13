/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

//损失情况列表(隶属于报案信息)
/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BiReportLossDataVo {
	@XmlElement(name = "LossFeeType")
	private String lossFeeType;//损失项赔偿类型代码；参见代码


	/** 
	 * @return 返回 lossFeeType  损失项赔偿类型代码；参见代码
	 */ 
	public String getLossFeeType(){ 
	    return lossFeeType;
	}

	/** 
	 * @param lossFeeType 要设置的 损失项赔偿类型代码；参见代码
	 */ 
	public void setLossFeeType(String lossFeeType){ 
	    this.lossFeeType=lossFeeType;
	}



}
