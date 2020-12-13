package ins.sino.claimcar.claim.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
@XStreamAlias("Packet")
public class PacketSdVo implements Serializable{
 @XStreamAlias("type")
 @XStreamAsAttribute
 private String type="REQUEST";
 @XStreamAlias("version")
 @XStreamAsAttribute
 private String version="1.0";
 @XStreamAlias("Head")
 private RequestSdHeadVo headVo;
 @XStreamAlias("Body")
 private RequestSdBodyVo bodyVo;
public RequestSdHeadVo getHeadVo() {
	return headVo;
}
public void setHeadVo(RequestSdHeadVo headVo) {
	this.headVo = headVo;
}
public RequestSdBodyVo getBodyVo() {
	return bodyVo;
}
public void setBodyVo(RequestSdBodyVo bodyVo) {
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
