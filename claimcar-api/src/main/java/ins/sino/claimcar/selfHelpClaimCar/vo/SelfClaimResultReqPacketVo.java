package ins.sino.claimcar.selfHelpClaimCar.vo;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;


@XStreamAlias("PACKET")
public class SelfClaimResultReqPacketVo {
 @XStreamAsAttribute
 @XStreamAlias("type")
 private String type="REQUEST";
 @XStreamAsAttribute
 @XStreamAlias("version")
 private String version="1.0";
	@XStreamAlias("HEAD")
	private ResquestHeadVo reqHeadVo;
	@XStreamAlias("BODY")
	private SelfClaimResultReqBodyVo reqBodyVo;
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
	public ResquestHeadVo getReqHeadVo() {
		return reqHeadVo;
	}
	public void setReqHeadVo(ResquestHeadVo reqHeadVo) {
		this.reqHeadVo = reqHeadVo;
	}
	public SelfClaimResultReqBodyVo getReqBodyVo() {
		return reqBodyVo;
	}
	public void setReqBodyVo(SelfClaimResultReqBodyVo reqBodyVo) {
		this.reqBodyVo = reqBodyVo;
	}
	

}
