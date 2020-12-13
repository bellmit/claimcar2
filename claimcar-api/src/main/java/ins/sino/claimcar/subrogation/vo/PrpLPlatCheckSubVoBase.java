package ins.sino.claimcar.subrogation.vo;

// Generated by Hibernate Tools 3.2.5 (sinosoft version), Don't modify!
import java.math.BigDecimal;
import java.util.Date;


/**
 * Vo Base Class of PO PrpLPlatCheckSub
 */ 
public class PrpLPlatCheckSubVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String recoveryCode;
	private BigDecimal recoverAmount;
	private BigDecimal compensateAmount;
	private Date checkDate;
	private String checkOpinion;
	private String checkOwnType;
	private String checkStats;
	private String claimStatus;
	private String claimProgress;

    protected PrpLPlatCheckSubVoBase() {
	
    }

    
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRecoveryCode() {
		return this.recoveryCode;
	}

	public void setRecoveryCode(String recoveryCode) {
		this.recoveryCode = recoveryCode;
	}

	public BigDecimal getRecoverAmount() {
		return this.recoverAmount;
	}

	public void setRecoverAmount(BigDecimal recoverAmount) {
		this.recoverAmount = recoverAmount;
	}

	public BigDecimal getCompensateAmount() {
		return this.compensateAmount;
	}

	public void setCompensateAmount(BigDecimal compensateAmount) {
		this.compensateAmount = compensateAmount;
	}

	public Date getCheckDate() {
		return this.checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	public String getCheckOpinion() {
		return this.checkOpinion;
	}

	public void setCheckOpinion(String checkOpinion) {
		this.checkOpinion = checkOpinion;
	}

	public String getCheckOwnType() {
		return this.checkOwnType;
	}

	public void setCheckOwnType(String checkOwnType) {
		this.checkOwnType = checkOwnType;
	}

	public String getCheckStats() {
		return this.checkStats;
	}

	public void setCheckStats(String checkStats) {
		this.checkStats = checkStats;
	}

	public String getClaimStatus() {
		return this.claimStatus;
	}

	public void setClaimStatus(String claimStatus) {
		this.claimStatus = claimStatus;
	}

	public String getClaimProgress() {
		return this.claimProgress;
	}

	public void setClaimProgress(String claimProgress) {
		this.claimProgress = claimProgress;
	}
}