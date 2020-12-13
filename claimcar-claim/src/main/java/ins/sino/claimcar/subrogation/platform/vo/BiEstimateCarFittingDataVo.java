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
public class BiEstimateCarFittingDataVo {
	@XmlElement(name = "ChangeOrRepair")
	private String changeOrRepair;//更换或修理标志；参见代码

	@XmlElement(name = "FittingName")
	private String fittingName;//更换/修理配件名称

	@XmlElement(name = "FittingNum")
	private String fittingNum;//更换/修理配件件数


	/** 
	 * @return 返回 changeOrRepair  更换或修理标志；参见代码
	 */ 
	public String getChangeOrRepair(){ 
	    return changeOrRepair;
	}

	/** 
	 * @param changeOrRepair 要设置的 更换或修理标志；参见代码
	 */ 
	public void setChangeOrRepair(String changeOrRepair){ 
	    this.changeOrRepair=changeOrRepair;
	}

	/** 
	 * @return 返回 fittingName  更换/修理配件名称
	 */ 
	public String getFittingName(){ 
	    return fittingName;
	}

	/** 
	 * @param fittingName 要设置的 更换/修理配件名称
	 */ 
	public void setFittingName(String fittingName){ 
	    this.fittingName=fittingName;
	}

	/** 
	 * @return 返回 fittingNum  更换/修理配件件数
	 */ 
	public String getFittingNum(){ 
	    return fittingNum;
	}

	/** 
	 * @param fittingNum 要设置的 更换/修理配件件数
	 */ 
	public void setFittingNum(String fittingNum){ 
	    this.fittingNum=fittingNum;
	}



	
	
}
