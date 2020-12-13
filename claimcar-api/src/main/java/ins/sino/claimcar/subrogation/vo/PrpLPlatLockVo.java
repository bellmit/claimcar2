package ins.sino.claimcar.subrogation.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Custom VO class of PO PrpLPlatLock
 */
public class PrpLPlatLockVo extends PrpLPlatLockVoBase implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal thisPaid;
	/** 结算查询是否返回值 0 空 1 是 */
	private String accountInfoFlag;

	private BigDecimal recoveryAmountMin;// 追偿金额

	private BigDecimal recoveryAmountMax;// 追偿金额

	private BigDecimal payAmountMin;// 清付金额

	private BigDecimal payAmountMax;// 清付金额

	private Date payStartTime;// 结算起始时间

	private Date payEndTime;

	public BigDecimal getThisPaid() {
		return thisPaid;
	}

	public void setThisPaid(BigDecimal thisPaid) {
		this.thisPaid = thisPaid;
	}

	public String getAccountInfoFlag() {
		return accountInfoFlag;
	}

	public void setAccountInfoFlag(String accountInfoFlag) {
		this.accountInfoFlag = accountInfoFlag;
	}

	public BigDecimal getRecoveryAmountMin() {
		return recoveryAmountMin;
	}

	public void setRecoveryAmountMin(BigDecimal recoveryAmountMin) {
		this.recoveryAmountMin = recoveryAmountMin;
	}

	public BigDecimal getRecoveryAmountMax() {
		return recoveryAmountMax;
	}

	public void setRecoveryAmountMax(BigDecimal recoveryAmountMax) {
		this.recoveryAmountMax = recoveryAmountMax;
	}

	public BigDecimal getPayAmountMin() {
		return payAmountMin;
	}

	public void setPayAmountMin(BigDecimal payAmountMin) {
		this.payAmountMin = payAmountMin;
	}

	public BigDecimal getPayAmountMax() {
		return payAmountMax;
	}

	public void setPayAmountMax(BigDecimal payAmountMax) {
		this.payAmountMax = payAmountMax;
	}

	public Date getPayStartTime() {
		return payStartTime;
	}

	public void setPayStartTime(Date payStartTime) {
		this.payStartTime = payStartTime;
	}

	public Date getPayEndTime() {
		return payEndTime;
	}

	public void setPayEndTime(Date payEndTime) {
		this.payEndTime = payEndTime;
	}

}
