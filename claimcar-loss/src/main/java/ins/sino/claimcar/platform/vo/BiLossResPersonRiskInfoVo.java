package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlElement;

public class BiLossResPersonRiskInfoVo {

	@XmlElement(name = "PersonRiskType", required = true)
	private String personRiskType;// 人员风险类型，参见代码

	/**
	 * @return 返回 personRiskType 人员风险类型，参见代码
	 */
	public String getPersonRiskType() {
		return personRiskType;
	}

	/**
	 * @param personRiskType 要设置的 人员风险类型，参见代码
	 */
	public void setPersonRiskType(String personRiskType) {
		this.personRiskType = personRiskType;
	}

}
