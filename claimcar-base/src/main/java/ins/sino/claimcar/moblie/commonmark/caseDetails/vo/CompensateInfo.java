package ins.sino.claimcar.moblie.commonmark.caseDetails.vo;


import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 赔付信息
 * @author zjd
 *
 */
@XStreamAlias("COMPENSATEINFO")
public class CompensateInfo implements Serializable{
	
	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("STIPOLICYNO")
	private String stiPolicyNo; //交强险保单
	
	@XStreamAlias("BUSIPOLICYNO")
	private String busiPolicyNo; //商业险保单
	
	@XStreamAlias("NAME")
	private String name; //收款人姓名
	
	@XStreamAlias("EXCEPTIONSLOGO")
	private String exceptionsLogo; //例外标志
	
	@XStreamAlias("EXCEPTIONREASON")
	private String exceptionReason; //例外原因
	
	@XStreamAlias("ACCOUNTNO")
	private String accountNo; //收款人账号
	
	@XStreamAlias("BANKNAME")
	private String bankName; //开户银行
	
	@XStreamAlias("CPSMONEY")
	private String cpsMoney; //赔款金额
	
	@XStreamAlias("CPSCLAIMTYPE")
	private String cpsClaimType; //支付状态
	
	@XStreamAlias("CPSTIME")
	private String cpsTime; //支付时间

	public String getStiPolicyNo() {
		return stiPolicyNo;
	}

	public void setStiPolicyNo(String stiPolicyNo) {
		this.stiPolicyNo = stiPolicyNo;
	}

	public String getBusiPolicyNo() {
		return busiPolicyNo;
	}

	public void setBusiPolicyNo(String busiPolicyNo) {
		this.busiPolicyNo = busiPolicyNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExceptionsLogo() {
		return exceptionsLogo;
	}

	public void setExceptionsLogo(String exceptionsLogo) {
		this.exceptionsLogo = exceptionsLogo;
	}

	public String getExceptionReason() {
		return exceptionReason;
	}

	public void setExceptionReason(String exceptionReason) {
		this.exceptionReason = exceptionReason;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCpsMoney() {
		return cpsMoney;
	}

	public void setCpsMoney(String cpsMoney) {
		this.cpsMoney = cpsMoney;
	}

	public String getCpsClaimType() {
		return cpsClaimType;
	}

	public void setCpsClaimType(String cpsClaimType) {
		this.cpsClaimType = cpsClaimType;
	}

	public String getCpsTime() {
		return cpsTime;
	}

	public void setCpsTime(String cpsTime) {
		this.cpsTime = cpsTime;
	}
	
	
}
