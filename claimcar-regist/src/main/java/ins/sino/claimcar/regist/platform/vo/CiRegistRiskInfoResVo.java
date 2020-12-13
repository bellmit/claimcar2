package ins.sino.claimcar.regist.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 交强报案平台返回信息RiskInfo类
 * @author dengkk
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiRegistRiskInfoResVo {

	@XmlElement(name="RISK_TYPE")
	private String riskType;//风险提示代码

	public String getRiskType() {
		return riskType;
	}

	public void setRiskType(String riskType) {
		this.riskType = riskType;
	}
	
	
}
