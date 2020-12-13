/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

//人员伤亡情况列表(隶属于定核损信息)
/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BiEstimatePersonDataVo {
	@XmlElement(name = "PersonName", required = true)
	private String personName;//伤亡人员姓名

	@XmlElement(name = "InjuryType")
	private String injuryType;//伤情类别

	@XmlElement(name = "InjuryLevel")
	private String injuryLevel;//伤残程度


	/** 
	 * @return 返回 personName  伤亡人员姓名
	 */ 
	public String getPersonName(){ 
	    return personName;
	}

	/** 
	 * @param personName 要设置的 伤亡人员姓名
	 */ 
	public void setPersonName(String personName){ 
	    this.personName=personName;
	}

	/** 
	 * @return 返回 injuryType  伤情类别
	 */ 
	public String getInjuryType(){ 
	    return injuryType;
	}

	/** 
	 * @param injuryType 要设置的 伤情类别
	 */ 
	public void setInjuryType(String injuryType){ 
	    this.injuryType=injuryType;
	}

	/** 
	 * @return 返回 injuryLevel  伤残程度
	 */ 
	public String getInjuryLevel(){ 
	    return injuryLevel;
	}

	/** 
	 * @param injuryLevel 要设置的 伤残程度
	 */ 
	public void setInjuryLevel(String injuryLevel){ 
	    this.injuryLevel=injuryLevel;
	}



	
	/** 人员损失费用明细列表（隶属于人员损失情况） */
	@XmlElement(name = "LossFeeData")
	private List<BiPersonLossFeeDataVo> lossFeeDataList;

	

	
	
}
