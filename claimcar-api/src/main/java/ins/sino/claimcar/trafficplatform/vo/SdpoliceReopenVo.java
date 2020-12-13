package ins.sino.claimcar.trafficplatform.vo;



import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Packet")
public class SdpoliceReopenVo {
@XStreamAlias("Head")
private RequestHeadVo headVo;

@XStreamAlias("Body")
private RequestReopenBodyVo bodyVo;

public RequestHeadVo getHeadVo() {
	return headVo;
}

public void setHeadVo(RequestHeadVo headVo) {
	this.headVo = headVo;
}

public RequestReopenBodyVo getBodyVo() {
	return bodyVo;
}

public void setBodyVo(RequestReopenBodyVo bodyVo) {
	this.bodyVo = bodyVo;
}



}
