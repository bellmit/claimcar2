package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CiLossResInstitutionRiskInfoVo {

	@XmlElement(name = "INSTITUTION_RISK_TYPE", required = true)
	private String institutionRiskType;// 机构风险类型，参见代码

	/**
	 * @return 返回 institutionRiskType 机构风险类型，参见代码
	 */
	public String getInstitutionRiskType() {
		return institutionRiskType;
	}

	/**
	 * @param institutionRiskType 要设置的 机构风险类型，参见代码
	 */
	public void setInstitutionRiskType(String institutionRiskType) {
		this.institutionRiskType = institutionRiskType;
	}

}
