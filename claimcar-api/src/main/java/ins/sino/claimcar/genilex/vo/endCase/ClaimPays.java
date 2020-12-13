package ins.sino.claimcar.genilex.vo.endCase;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ClaimPays")
public class ClaimPays implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("ClaimPay") 
	private ClaimPay claimPay;
	
	@XStreamAlias("AdvancedPays") 
	private List<AdvancedPay> advancedPays;

	public ClaimPay getClaimPay() {
		return claimPay;
	}

	public void setClaimPay(ClaimPay claimPay) {
		this.claimPay = claimPay;
	}

	public List<AdvancedPay> getAdvancedPays() {
		return advancedPays;
	}

	public void setAdvancedPays(List<AdvancedPay> advancedPays) {
		this.advancedPays = advancedPays;
	}

	
	
}
