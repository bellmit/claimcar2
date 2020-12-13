package ins.sino.claimcar.claim.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
@XStreamAlias("Packet")
public class ResPacketSdVo implements Serializable{
	@XStreamAlias("type")
	@XStreamAsAttribute
	private String type="RESPONSE";
	@XStreamAlias("version")
	@XStreamAsAttribute
	private String version="1.0";
    @XStreamAlias("Head")
	public ResHeadSdVo resHeadSdVo;

	public ResHeadSdVo getResHeadSdVo() {
		return resHeadSdVo;
	}

	public void setResHeadSdVo(ResHeadSdVo resHeadSdVo) {
		this.resHeadSdVo = resHeadSdVo;
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
