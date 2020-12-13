/******************************************************************************
 * CREATETIME : 2016年4月26日 下午2:47:00
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * <pre></pre>
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CICheckResInstitutionRiskInfoVo {

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
