package ins.sino.claimcar.trafficplatform.vo;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Packet")
public class SdpoliceCancleVo {
	 private static final long serialVersionUID = 1L;
	 @XStreamAlias("Head")
	 private RequestHeadVo headVo;
	 @XStreamAlias("Body")
     private RequestCancleBodyVo bodyVo;
	public RequestHeadVo getHeadVo() {
		return headVo;
	}
	public void setHeadVo(RequestHeadVo headVo) {
		this.headVo = headVo;
	}
	public RequestCancleBodyVo getBodyVo() {
		return bodyVo;
	}
	public void setBodyVo(RequestCancleBodyVo bodyVo) {
		this.bodyVo = bodyVo;
	}
}
