package ins.sino.claimcar.trafficplatform.vo;

import ins.sino.claimcar.trafficplatform.vo.RequestHeadVo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("Packet")
public class SdPoliceDLossVo implements Serializable{
 private static final long serialVersionUID = 1L;
 
 @XStreamAlias("Head")
 private RequestHeadVo headVo;
 @XStreamAlias("Body")
 private RequestDLossBodyVo bodyVo;
	 
	public RequestHeadVo getHeadVo() {
		return headVo;
	}
	public void setHeadVo(RequestHeadVo headVo) {
		this.headVo = headVo;
	}
	public RequestDLossBodyVo getBodyVo() {
		return bodyVo;
	}
	public void setBodyVo(RequestDLossBodyVo bodyVo) {
		this.bodyVo = bodyVo;
	}
	 
	 
}
