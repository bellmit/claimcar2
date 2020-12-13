package ins.sino.claimcar.genilex.vo.endCase;

import ins.sino.claimcar.genilex.vo.common.ClaimMain;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Entities")
public class EndCaseEntities implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("ClaimMains") 
	private List<ClaimMain>  claimMains;
	
	@XStreamAlias("Adjustments") 
	private List<Adjustment> adjustments;
	
	@XStreamAlias("CloseCases") 
	private List<CloseCase> closeCases;
	
	@XStreamAlias("ClaimPays") 
	private ClaimPays claimPays;
	

	public ClaimPays getClaimPays() {
		return claimPays;
	}

	public void setClaimPays(ClaimPays claimPays) {
		this.claimPays = claimPays;
	}

	public List<ClaimMain> getClaimMains() {
		return claimMains;
	}

	public void setClaimMains(List<ClaimMain> claimMains) {
		this.claimMains = claimMains;
	}

	public List<Adjustment> getAdjustments() {
		return adjustments;
	}

	public void setAdjustments(List<Adjustment> adjustments) {
		this.adjustments = adjustments;
	}

	public List<CloseCase> getCloseCases() {
		return closeCases;
	}

	public void setCloseCases(List<CloseCase> closeCases) {
		this.closeCases = closeCases;
	}

	
	
}
