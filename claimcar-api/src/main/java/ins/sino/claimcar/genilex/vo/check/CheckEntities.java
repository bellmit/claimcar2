package ins.sino.claimcar.genilex.vo.check;

import ins.sino.claimcar.genilex.vo.common.ClaimMain;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Entities")
public class CheckEntities implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("Surverys") 
	private List<Survery>  surverys;

	@XStreamAlias("ClaimMains") 
	private List<ClaimMain>  claimMains;
	
	public List<ClaimMain> getClaimMains() {
		return claimMains;
	}

	public void setClaimMains(List<ClaimMain> claimMains) {
		this.claimMains = claimMains;
	}


	public List<Survery> getSurverys() {
		return surverys;
	}

	public void setSurverys(List<Survery> surverys) {
		this.surverys = surverys;
	}
	
}
