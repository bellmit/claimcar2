/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

//车辆配件明细列表（隶属于车辆损失情况）
/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiEstimateCarFittingDataVo {
	/** 更换或修理标志 **/ 
	@XmlElement(name="CHANGE_OR_REPAIR")
	private String changeOrRepair; 
	
	/** 更换/修理配件名称 **/ 
	@XmlElement(name="FITTING_NAME")
	private String fittingName; 

	/** 更换/修理配件件数 **/ 
	@XmlElement(name="FITTING_NUM")
	private String fittingNum;

	public String getChangeOrRepair() {
		return changeOrRepair;
	}

	public void setChangeOrRepair(String changeOrRepair) {
		this.changeOrRepair = changeOrRepair;
	}

	public String getFittingName() {
		return fittingName;
	}

	public void setFittingName(String fittingName) {
		this.fittingName = fittingName;
	}

	public String getFittingNum() {
		return fittingNum;
	}

	public void setFittingNum(String fittingNum) {
		this.fittingNum = fittingNum;
	} 


	
	
}
