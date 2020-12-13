/******************************************************************************
 * CREATETIME : 2016年4月26日 下午2:42:52
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
public class CICheckResPersonRiskInfoVo {

	/** 人员风险类型 **/
	@XmlElement(name = "PERSON_RISK_TYPE")
	private String personRiskType;

	public String getPersonRiskType() {
		return personRiskType;
	}

	public void setPersonRiskType(String personRiskType) {
		this.personRiskType = personRiskType;
	}

}
