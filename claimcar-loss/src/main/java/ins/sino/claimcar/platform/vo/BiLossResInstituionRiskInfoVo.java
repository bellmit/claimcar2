package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlElement;

public class BiLossResInstituionRiskInfoVo {

	@XmlElement(name = "InstituionRiskType", required = true)
	private String instituionRiskType;// 机构风险类型，参见代码

	/**
	 * @return 返回 instituionRiskType 机构风险类型，参见代码
	 */
	public String getInstituionRiskType() {
		return instituionRiskType;
	}

	/**
	 * @param instituionRiskType 要设置的 机构风险类型，参见代码
	 */
	public void setInstituionRiskType(String instituionRiskType) {
		this.instituionRiskType = instituionRiskType;
	}

}
