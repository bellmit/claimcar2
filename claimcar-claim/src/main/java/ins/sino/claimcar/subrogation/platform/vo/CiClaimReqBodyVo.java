/******************************************************************************
* CREATETIME : 2016年3月17日 下午7:16:59
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * 承保保单
 * @CreateTime 2016年3月17日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class CiClaimReqBodyVo {

	@XmlElement(name = "BasePart")
	private CiClaimBaseVo CiClaimBaseVo;

	public CiClaimBaseVo getCiClaimBaseVo() {
		return CiClaimBaseVo;
	}

	public void setCiClaimBaseVo(CiClaimBaseVo ciClaimBaseVo) {
		CiClaimBaseVo = ciClaimBaseVo;
	}




	

	
}
