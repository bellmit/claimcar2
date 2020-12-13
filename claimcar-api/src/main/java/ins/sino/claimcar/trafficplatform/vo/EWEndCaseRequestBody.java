package ins.sino.claimcar.trafficplatform.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;


@XStreamAlias("Body")
public class EWEndCaseRequestBody implements Serializable{

	/**  */
	private static final long serialVersionUID = -5078370412227486079L;
	
	@XStreamAlias("BasePart") 
	private EWEndCaseBasePartVo basePart;
	
	@XStreamImplicit(itemFieldName="ClaimCoverData")
	private List<EWEndCaseClaimCoverDataVo> claimCoverData;

	
	public EWEndCaseBasePartVo getBasePart() {
		return basePart;
	}

	
	public void setBasePart(EWEndCaseBasePartVo basePart) {
		this.basePart = basePart;
	}


	
	public List<EWEndCaseClaimCoverDataVo> getClaimCoverData() {
		return claimCoverData;
	}


	
	public void setClaimCoverData(List<EWEndCaseClaimCoverDataVo> claimCoverData) {
		this.claimCoverData = claimCoverData;
	}

	
	
	
}

