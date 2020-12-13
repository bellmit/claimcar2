package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CiLossResPersonRiskInfoVo {

	@XmlElement(name = "PERSON_RISK_TYPE", required = true)
	private String personRiskType;//人员风险类型，参见代码


	/** 
	 * @return 返回 personRiskType  人员风险类型，参见代码
	 */ 
	public String getPersonRiskType(){ 
	    return personRiskType;
	}

	/** 
	 * @param personRiskType 要设置的 人员风险类型，参见代码
	 */ 
	public void setPersonRiskType(String personRiskType){ 
	    this.personRiskType=personRiskType;
	}



}
