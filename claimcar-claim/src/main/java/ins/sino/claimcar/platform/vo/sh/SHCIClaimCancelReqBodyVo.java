package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class SHCIClaimCancelReqBodyVo {

	@XmlElement(name = "BASE_PART")
	private SHCIClaimCancelReqBasePartyVo basePartyVo;//

	public SHCIClaimCancelReqBasePartyVo getBasePartyVo() {
		return basePartyVo;
	}

	public void setBasePartyVo(SHCIClaimCancelReqBasePartyVo basePartyVo) {
		this.basePartyVo = basePartyVo;
	}

}
