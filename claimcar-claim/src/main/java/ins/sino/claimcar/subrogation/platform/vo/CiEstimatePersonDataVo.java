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
public class CiEstimatePersonDataVo {
	/** 伤亡人员姓名 **/ 
	@XmlElement(name="PERSON_NAME", required = true)
	private String personName; 

	/** 伤情类别 **/ 
	@XmlElement(name="INJURY_TYPE")
	private String injuryType; 

	/** 伤残程度 **/ 
	@XmlElement(name="INJURY_LEVEL")
	private String injuryLevel;
	
	/** 人员损失费用明细列表（隶属于人员损失情况） */
	@XmlElementWrapper(name = "LOSS_FEE_LIST")
	@XmlElement(name = "LOSS_FEE_DATA")
	private List<CiPersonLossFeeDataVo> lossFeeDataList;

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getInjuryType() {
		return injuryType;
	}

	public void setInjuryType(String injuryType) {
		this.injuryType = injuryType;
	}

	public String getInjuryLevel() {
		return injuryLevel;
	}

	public void setInjuryLevel(String injuryLevel) {
		this.injuryLevel = injuryLevel;
	}

	
	public List<CiPersonLossFeeDataVo> getLossFeeDataList() {
		return lossFeeDataList;
	}

	
	public void setLossFeeDataList(List<CiPersonLossFeeDataVo> lossFeeDataList) {
		this.lossFeeDataList = lossFeeDataList;
	} 



	
	
}
