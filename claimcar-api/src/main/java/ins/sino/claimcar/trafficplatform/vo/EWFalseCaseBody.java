package ins.sino.claimcar.trafficplatform.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Body")
public class EWFalseCaseBody  implements Serializable {

	private static final long serialVersionUID = 8423652723600188374L;
	
	@XStreamAlias("BaseInfo")
	private EWFalseCaseBaseInfo baseInfo;

	public EWFalseCaseBaseInfo getBaseInfo() {
		return baseInfo;
	}

	public void setBaseInfo(EWFalseCaseBaseInfo baseInfo) {
		this.baseInfo = baseInfo;
	}
	
}
