package ins.sino.claimcar.trafficplatform.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("Packet")	
public class SdResponseVo {
	@XStreamAlias("Head")	
	private ResponseHeadVo headVo;

	public ResponseHeadVo getHeadVo() {
		return headVo;
	}

	public void setHeadVo(ResponseHeadVo headVo) {
		this.headVo = headVo;
	}
}
