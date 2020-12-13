package ins.sino.claimcar.selfHelpClaimCar.vo;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("PACKET")
public class SelfClaimDealFlowResPacketVo {
@XStreamAsAttribute
@XStreamAlias("type")
 private String type="REQUEST";
 @XStreamAsAttribute
 @XStreamAlias("version")
 private String version="1.0";

 @XStreamAlias("HEAD")
 private ResponseHeadVo reqHeadVo;
 @XStreamAlias("BODY")
 private SelfClaimDealFlowResBodyVo reqBodyVo;

 public ResponseHeadVo getReqHeadVo() {
	return reqHeadVo;
 }
 public void setReqHeadVo(ResponseHeadVo reqHeadVo) {
	this.reqHeadVo = reqHeadVo;
 }
 public SelfClaimDealFlowResBodyVo getReqBodyVo() {
	return reqBodyVo;
 }
 public void setReqBodyVo(SelfClaimDealFlowResBodyVo reqBodyVo) {
	this.reqBodyVo = reqBodyVo;
 }

}
