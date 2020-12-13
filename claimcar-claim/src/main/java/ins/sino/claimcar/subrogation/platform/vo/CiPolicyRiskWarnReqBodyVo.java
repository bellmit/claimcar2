/******************************************************************************
* CREATETIME : 2016年3月16日 上午11:55:40
* FILE       : ins.sino.claimcar.carplatform.vo.PolicyRiskWarnBodyVo
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class CiPolicyRiskWarnReqBodyVo {

	@XmlElement(name = "BASE_PART")
	private CiPolicyRiskWarnBaseVo policyRiskWarnBaseVo;

	
	public CiPolicyRiskWarnBaseVo getPolicyRiskWarnBaseVo() {
		return policyRiskWarnBaseVo;
	}

	
	public void setPolicyRiskWarnBaseVo(CiPolicyRiskWarnBaseVo policyRiskWarnBaseVo) {
		this.policyRiskWarnBaseVo = policyRiskWarnBaseVo;
	}

	
	
}
