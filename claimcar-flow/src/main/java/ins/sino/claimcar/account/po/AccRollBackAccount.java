package ins.sino.claimcar.account.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "ACCROLLBACKACCOUNT_PK", allocationSize = 10)
@Table(name = "ACCROLLBACKACCOUNT")
public class AccRollBackAccount implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private String certiNo;
	private String voucherNo;
	private String yearMonth;
	private String centerCode;
	private String suffixNo;
	private String payType;
	private String originalAccountNo;
	private String targetAccountNo;
	private String rollBackCode;
	private String rollBackTime;
	private String modifyCode;
	private String modifyTime;
	private String status;
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence")
	@Column(name = "CERTINO", length=25)
	public String getCertiNo() {
		return certiNo;
	}
	public void setCertiNo(String certiNo) {
		this.certiNo = certiNo;
	}
	@Column(name = "VOUCHERNO", length=30)
	public String getVoucherNo() {
		return voucherNo;
	}
	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}
	@Column(name = "YEARMONTH", length=6)
	public String getYearMonth() {
		return yearMonth;
	}
	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}
	@Column(name = "CENTERCODE", length=10)
	public String getCenterCode() {
		return centerCode;
	}
	public void setCenterCode(String centerCode) {
		this.centerCode = centerCode;
	}
	@Column(name = "SUFFIXNO", length=10)
	public String getSuffixNo() {
		return suffixNo;
	}
	public void setSuffixNo(String suffixNo) {
		this.suffixNo = suffixNo;
	}
	@Column(name = "PAYTYPE", length=3)
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	@Column(name = "ORIGINALACCOUNTNO", length=30)
	public String getOriginalAccountNo() {
		return originalAccountNo;
	}
	public void setOriginalAccountNo(String originalAccountNo) {
		this.originalAccountNo = originalAccountNo;
	}
	@Column(name = "TARGETACCOUNTNO", length=30)
	public String getTargetAccountNo() {
		return targetAccountNo;
	}
	public void setTargetAccountNo(String targetAccountNo) {
		this.targetAccountNo = targetAccountNo;
	}
	@Column(name = "ROLLBACKCODE", length=20)
	public String getRollBackCode() {
		return rollBackCode;
	}
	public void setRollBackCode(String rollBackCode) {
		this.rollBackCode = rollBackCode;
	}
	@Column(name = "ROLLBACKTIME", length=20)
	public String getRollBackTime() {
		return rollBackTime;
	}
	public void setRollBackTime(String rollBackTime) {
		this.rollBackTime = rollBackTime;
	}
	@Column(name = "MODIFYCODE", length=20)
	public String getModifyCode() {
		return modifyCode;
	}
	public void setModifyCode(String modifyCode) {
		this.modifyCode = modifyCode;
	}
	@Column(name = "MODIFYTIME", length=20)
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	@Column(name = "STATUS", length=10)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
