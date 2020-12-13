/******************************************************************************
* CREATETIME : 2016年3月17日 下午7:39:34
******************************************************************************/
package ins.sino.claimcar.subrogation.vo;

import java.io.Serializable;
import java.util.List;

//人员损失情况列表(隶属于定核损信息)
/**
 * @author ★YangKun
 * @CreateTime 2016年3月17日
 */
public class EstimatePersonDataVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/** 伤亡人员姓名 **/ 
	private String personName; 

	/** 伤情类别 **/ 
	private String injuryType; 

	/** 伤残程度 **/ 
	private String injuryLevel;
	
	/** 人员损失费用明细列表（隶属于人员损失情况） */
	private List<PersonLossFeeDataVo> lossFeeDataList;

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

	public List<PersonLossFeeDataVo> getLossFeeDataList() {
		return lossFeeDataList;
	}

	public void setLossFeeDataList(List<PersonLossFeeDataVo> lossFeeDataList) {
		this.lossFeeDataList = lossFeeDataList;
	}
	
}
