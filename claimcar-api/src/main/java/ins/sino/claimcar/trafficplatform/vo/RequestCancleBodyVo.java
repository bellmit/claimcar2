package ins.sino.claimcar.trafficplatform.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Body")
public class RequestCancleBodyVo{
	@XStreamAlias("BasePart")
	private RequestCancleBasePartVo basePartVo;

	public RequestCancleBasePartVo getBasePartVo() {
		return basePartVo;
	}

	public void setBasePartVo(RequestCancleBasePartVo basePartVo) {
		this.basePartVo = basePartVo;
	}
	
}
