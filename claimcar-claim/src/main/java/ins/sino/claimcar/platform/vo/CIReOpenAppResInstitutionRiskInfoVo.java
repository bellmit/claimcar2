package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CIReOpenAppResInstitutionRiskInfoVo {
	
	/** 机构风险类型 **/
	@XmlElement(name = "INSTITUTION_RISK_TYPE")
	private String institutionRiskType;

	public String getInstitutionRiskType() {
		return institutionRiskType;
	}

	public void setInstitutionRiskType(String institutionRiskType) {
		this.institutionRiskType = institutionRiskType;
	}

}
