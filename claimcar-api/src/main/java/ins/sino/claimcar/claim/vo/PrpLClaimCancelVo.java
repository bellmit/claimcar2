package ins.sino.claimcar.claim.vo;

import java.util.Date;

/**
 * Custom VO class of PO PrpLClaim
 */ 
public class PrpLClaimCancelVo extends PrpLClaimVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L; 
	
	private String insuredName;//被保险人
	
	private Date StartDate;//起保日期
	
	private Date endDate;//终保日期

	private Date claimCancelTime;//立案时间
	
	private String applyReason;//注销原因
	
	private String recoverReason;//注销/拒赔恢复原因
	
	private Date claimRecoverTime;//恢复日期
	
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

	public Date getClaimCancelTime() {
		return claimCancelTime;
	}

	public void setClaimCancelTime(Date claimCancelTime) {
		this.claimCancelTime = claimCancelTime;
	}

	public String getApplyReason() {
		return applyReason;
	}

	public void setApplyReason(String applyReason) {
		this.applyReason = applyReason;
	}

	public String getRecoverReason() {
		return recoverReason;
	}

	public void setRecoverReason(String recoverReason) {
		this.recoverReason = recoverReason;
	}

	public Date getClaimRecoverTime() {
		return claimRecoverTime;
	}

	public void setClaimRecoverTime(Date claimRecoverTime) {
		this.claimRecoverTime = claimRecoverTime;
	}
	
	
	
}
