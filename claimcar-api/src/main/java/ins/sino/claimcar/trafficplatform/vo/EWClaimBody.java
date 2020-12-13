package ins.sino.claimcar.trafficplatform.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Body")
public class EWClaimBody implements Serializable {

	private static final long serialVersionUID = 8423652723600188374L;
	
	@XStreamAlias("BasePart")
	private EWClaimBasePart basePart;

	public EWClaimBasePart getBasePart() {
		return basePart;
	}

	public void setBasePart(EWClaimBasePart basePart) {
		this.basePart = basePart;
	}
	
	
}
