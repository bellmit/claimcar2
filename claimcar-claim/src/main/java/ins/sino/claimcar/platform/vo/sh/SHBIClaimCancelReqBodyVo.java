package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class SHBIClaimCancelReqBodyVo {

	@XmlElement(name = "BASE_PART")
	private SHBIClaimCancelReqBasePartyVo basePartVo;//

	
	
	public SHBIClaimCancelReqBasePartyVo getBasePartVo() {
		return basePartVo;
	}

	public void setBasePartVo(SHBIClaimCancelReqBasePartyVo basePartVo) {
		this.basePartVo = basePartVo;
	}

}
