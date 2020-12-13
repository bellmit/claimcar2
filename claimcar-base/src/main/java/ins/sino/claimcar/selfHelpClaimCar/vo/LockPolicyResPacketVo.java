package ins.sino.claimcar.selfHelpClaimCar.vo;

import ins.sino.claimcar.selfHelpClaimCar.vo.ResponseHeadVo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("PACKET")
public class LockPolicyResPacketVo {
 @XStreamAsAttribute
 @XStreamAlias("type")
 private String type="RESPONSE";
 @XStreamAsAttribute
 @XStreamAlias("version")
 private String version="1.0";
 @XStreamAlias("HEAD")
 private ResponseHeadVo headVo;
 @XStreamAlias("BODY")
 private LockPolicyResBodyVo bodyVo;

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
 
 public ResponseHeadVo getHeadVo() {
	return headVo;
}
public void setHeadVo(ResponseHeadVo headVo) {
	this.headVo = headVo;
}
public LockPolicyResBodyVo getBodyVo() {
	return bodyVo;
}
public void setBodyVo(LockPolicyResBodyVo bodyVo) {
	this.bodyVo = bodyVo;
}
 
}
