package ins.sino.claimcar.genilex.vo;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Entities")
public class EntitiesVo {
	@XStreamAlias("Reports")
	private List<Report> reports;//报案信息列表
	
	@XStreamAlias("Policys")
	private List<Policy> Policys;//保单信息列表
	
	@XStreamAlias("ClaimMains")
	private List<ClaimMain> claimMains;//赔案主档信息

	public List<Report> getReports() {
		return reports;
	}

	public void setReports(List<Report> reports) {
		this.reports = reports;
	}

	public List<Policy> getPolicys() {
		return Policys;
	}

	public void setPolicys(List<Policy> policys) {
		Policys = policys;
	}

	public List<ClaimMain> getClaimMains() {
		return claimMains;
	}

	public void setClaimMains(List<ClaimMain> claimMains) {
		this.claimMains = claimMains;
	}

	
}
