package ins.sino.claimcar.sdpolice.policeInfoVo;

import ins.sino.claimcar.trafficplatform.vo.RequestHeadVo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Packet")
public class SdpoliceInfoVo{
	 @XStreamAlias("Head")
	 private RequestHeadVo headVo;
	 @XStreamAlias("Body")
	 private RequestPoliceInfoBodyVo bodyVo;
	public RequestHeadVo getHeadVo() {
		return headVo;
	}
	public void setHeadVo(RequestHeadVo headVo) {
		this.headVo = headVo;
	}
	public RequestPoliceInfoBodyVo getBodyVo() {
		return bodyVo;
	}
	public void setBodyVo(RequestPoliceInfoBodyVo bodyVo) {
		this.bodyVo = bodyVo;
	}
	 
   
}
