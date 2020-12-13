package ins.sino.claimcar.base.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.util.Date;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@TableGenerator(name = "utiSequence", table = "UTISEQUENCE", pkColumnName = "GenName", valueColumnName = "GenValue", pkColumnValue = "PRPJLINKACCOUNT_PK", allocationSize = 10)
@Table(name = "PRPJLINKACCOUNT")
public class PrpJLinkAccount implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	private String linkNo;
	private String certiType;
	private String certiNo;
	private String payrefReason;
	private String claimNo;
	private String policyNo;
	private String comCode;
	private Double planFee;
	private String clientNo;
	private String accountNo;
	private String payReasonFlag;
	private String payReason;
	private Date operateDate;
	private String operateCode;
	
	private String operateBranch;
	private String reMark;
	private String flag;
	private String sNo;
	private Double oriplanFee;
	private Double commission;
	private String licenseNo;
	private String parentPayObject;
	private String centiType;
	private String centiCode;
	private String subPayObject;
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "utiSequence")
	@Column(name = "LINKNO", length=25)
	public String getLinkNo() {
		return linkNo;
	}
	public void setLinkNo(String linkNo) {
		this.linkNo = linkNo;
	}
	@Column(name = "CERTITYPE", length=30)
	public String getCertiType() {
		return certiType;
	}
	public void setCertiType(String certiType) {
		this.certiType = certiType;
	}
	@Column(name = "CERTINO", length=30)
	public String getCertiNo() {
		return certiNo;
	}
	public void setCertiNo(String certiNo) {
		this.certiNo = certiNo;
	}
	@Column(name = "PAYREFREASON", length=3)
	public String getPayrefReason() {
		return payrefReason;
	}
	public void setPayrefReason(String payrefReason) {
		this.payrefReason = payrefReason;
	}
	@Column(name = "CLAIMNO", length=30)
	public String getClaimNo() {
		return claimNo;
	}
	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}
	@Column(name = "POLICYNO", length=30)
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	@Column(name = "COMCODE", length=30)
	public String getComCode() {
		return comCode;
	}
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	@Column(name = "PLANFEE", length=30)
	public Double getPlanFee() {
		return planFee;
	}
	
	public void setPlanFee(Double planFee) {
		this.planFee = planFee;
	}
	@Column(name = "CLIENTNO", length=30)
	public String getClientNo() {
		return clientNo;
	}
	public void setClientNo(String clientNo) {
		this.clientNo = clientNo;
	}
	@Column(name = "ACCOUNTNO", length=30)
	public String getAccountNo() {
		return accountNo;
	}
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	@Column(name = "PAYREASONFLAG", length=30)
	public String getPayReasonFlag() {
		return payReasonFlag;
	}
	public void setPayReasonFlag(String payReasonFlag) {
		this.payReasonFlag = payReasonFlag;
	}
	@Column(name = "PAYREASON", length=30)
	public String getPayReason() {
		return payReason;
	}
	public void setPayReason(String payReason) {
		this.payReason = payReason;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OPERATEDATE", length=7)
	public Date getOperateDate() {
		return operateDate;
	}
	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}
	@Column(name = "OPERATECODE", length=30)
	public String getOperateCode() {
		return operateCode;
	}
	public void setOperateCode(String operateCode) {
		this.operateCode = operateCode;
	}
	@Column(name = "OPERATEBRANCH", length=30)
	public String getOperateBranch() {
		return operateBranch;
	}
	public void setOperateBranch(String operateBranch) {
		this.operateBranch = operateBranch;
	}
	@Column(name = "REMARK", length=30)
	public String getReMark() {
		return reMark;
	}
	public void setReMark(String reMark) {
		this.reMark = reMark;
	}
	@Column(name = "FLAG", length=30)
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	@Column(name = "SNO", length=30)
	public String getsNo() {
		return sNo;
	}
	public void setsNo(String sNo) {
		this.sNo = sNo;
	}
	@Column(name = "ORIPLANFEE", length=30)
	public Double getOriplanFee() {
		return oriplanFee;
	}
	public void setOriplanFee(Double oriplanFee) {
		this.oriplanFee = oriplanFee;
	}
	@Column(name = "COMMISSION", length=30)
	public Double getCommission() {
		return commission;
	}
	public void setCommission(Double commission) {
		this.commission = commission;
	}
	@Column(name = "LICENSENO", length=30)
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	@Column(name = "PARENTPAYOBJECT", length=30)
	public String getParentPayObject() {
		return parentPayObject;
	}
	public void setParentPayObject(String parentPayObject) {
		this.parentPayObject = parentPayObject;
	}
	@Column(name = "CENTITYPE", length=30)
	public String getCentiType() {
		return centiType;
	}
	public void setCentiType(String centiType) {
		this.centiType = centiType;
	}
	@Column(name = "CENTICODE", length=30)
	public String getCentiCode() {
		return centiCode;
	}
	public void setCentiCode(String centiCode) {
		this.centiCode = centiCode;
	}
	@Column(name = "SUBPAYOBJECT", length=30)
	public String getSubPayObject() {
		return subPayObject;
	}
	public void setSubPayObject(String subPayObject) {
		this.subPayObject = subPayObject;
	}
	
				

}
