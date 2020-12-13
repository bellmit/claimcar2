package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class BIClaimCancelReqBodyVo {

	@XmlElement(name = "BasePart")
	private BIClaimCancelReqBasePartyVo reqBasePartyVo;// 基本信息

	public BIClaimCancelReqBasePartyVo getReqBasePartyVo() {
		return reqBasePartyVo;
	}

	public void setReqBasePartyVo(BIClaimCancelReqBasePartyVo reqBasePartyVo) {
		this.reqBasePartyVo = reqBasePartyVo;
	}

}
