/******************************************************************************
* CREATETIME : 2016年3月17日 下午7:16:59
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author ★YangKun
 * @CreateTime 2016年3月17日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class BiClaimRiskWarnReqBodyVo {

	@XmlElement(name = "BasePart")
	private BiClaimRiskWarnBaseVo biClaimRiskWarnBaseVo;

	public BiClaimRiskWarnBaseVo getBiClaimRiskWarnBaseVo() {
		return biClaimRiskWarnBaseVo;
	}

	public void setBiClaimRiskWarnBaseVo(BiClaimRiskWarnBaseVo biClaimRiskWarnBaseVo) {
		this.biClaimRiskWarnBaseVo = biClaimRiskWarnBaseVo;
	}
	

	
}
