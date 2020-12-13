package ins.sino.claimcar.selfHelpClaimCar.vo;


import ins.sino.claimcar.selfHelpClaimCar.vo.ResquestHeadVo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("PACKET")
public class LockPolicyReqPacketVo {
	 @XStreamAsAttribute
	 @XStreamAlias("type")
	 private String type="REQUEST";
	 @XStreamAsAttribute
	 @XStreamAlias("version")
	 private String version="1.0";
	@XStreamAlias("HEAD")
	private ResquestHeadVo headVo;
	@XStreamAlias("BODY")
	private LockPolicyNoReqBodyVo bodyVo;
	public ResquestHeadVo getHeadVo() {
		return headVo;
	}
	public void setHeadVo(ResquestHeadVo headVo) {
		this.headVo = headVo;
	}
	public LockPolicyNoReqBodyVo getBodyVo() {
		return bodyVo;
	}
	public void setBodyVo(LockPolicyNoReqBodyVo bodyVo) {
		this.bodyVo = bodyVo;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
    
}
