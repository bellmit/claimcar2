package ins.sino.claimcar.claim.vo;

import java.util.Date;

/**
 * Custom VO class of PO PrpLClaim
 */ 
public class PrpLClaimResultVo extends PrpLClaimVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	
	private String disasterId;//巨灾Id
	
	private String insuredName;//被保险人
	
	private Date StartDate;//起保日期
	
	private Date endDate;//终保日期

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public Date getStartDate() {
		return StartDate;
	}

	public void setStartDate(Date startDate) {
		StartDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getDisasterId() {
		return disasterId;
	}

	public void setDisasterId(String disasterId) {
		this.disasterId = disasterId;
	}

	
	
	
	
}
