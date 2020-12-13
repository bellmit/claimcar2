package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class CIClaimCancelReqBodyVo {

	@XmlElement(name = "BASE_PART")
	private CIClaimCancelReqBasePartyVo basePartyVo;// Y

	public CIClaimCancelReqBasePartyVo getBasePartyVo() {
		return this.basePartyVo;
	}

	public void setBasePartyVo(CIClaimCancelReqBasePartyVo basePartyVo) {
		this.basePartyVo = basePartyVo;
	}

}
