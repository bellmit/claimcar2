package ins.sino.claimcar.trafficplatform.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Body")
public class RequestReopenBodyVo {
@XStreamAlias("BasePart")
private RequestReopenBasePartVo basePartVo;

public RequestReopenBasePartVo getBasePartVo() {
	return basePartVo;
}

public void setBasePartVo(RequestReopenBasePartVo basePartVo) {
	this.basePartVo = basePartVo;
}


}
