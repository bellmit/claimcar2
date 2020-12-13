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
 * 风险预警理赔信息查询  交强 返回Body部分
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class CiClaimRiskWarnReqBodyVo {

	@XmlElement(name = "BASE_PART")
	private CiClaimRiskWarnBaseVo ciClaimRiskWarnBaseVo;

	public CiClaimRiskWarnBaseVo getCiClaimRiskWarnBaseVo() {
		return ciClaimRiskWarnBaseVo;
	}

	public void setCiClaimRiskWarnBaseVo(CiClaimRiskWarnBaseVo ciClaimRiskWarnBaseVo) {
		this.ciClaimRiskWarnBaseVo = ciClaimRiskWarnBaseVo;
	}

	


	
	
}
