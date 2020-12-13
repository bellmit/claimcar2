package ins.sino.claimcar.trafficplatform.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;


@XStreamAlias("Body")
public class EWVClaimRequestBody implements Serializable{

	/**  */
	private static final long serialVersionUID = -5078370412227486079L;
	
	@XStreamAlias("BasePart") 
	private EWVClaimBasePartVo basePart;
	
	@XStreamAlias("AdjustmentData") 
	private EWVClaimAdjustmentDataVo adjustmentData;

	
	public EWVClaimBasePartVo getBasePart() {
		return basePart;
	}

	
	public void setBasePart(EWVClaimBasePartVo basePart) {
		this.basePart = basePart;
	}

	
	public EWVClaimAdjustmentDataVo getAdjustmentData() {
		return adjustmentData;
	}

	
	public void setAdjustmentData(EWVClaimAdjustmentDataVo adjustmentData) {
		this.adjustmentData = adjustmentData;
	}
	
	
	
}

