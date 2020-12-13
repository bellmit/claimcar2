package ins.sino.claimcar.mobile.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("HISCLAIM")
public class HisclaimListVo  implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/*
	 * 报案号
	 */
	@XStreamAlias("REGISTNO")
	private String registNo;
	
	/*
	 * 交强险保单号
	 */
	@XStreamAlias("POLICYNO")
	private String policyNo;
	
	/*
	 * 商业险保单号
	 */
	@XStreamAlias("BUSIPOLICYNO")
	private String busipolicyNo;
	
	/*
	 * 车牌号
	 */
	@XStreamAlias("LICENSENO")
	private String licenseNo;
	
	/*
	 * 出险原因
	 */
	@XStreamAlias("DAMAGECODE")
	private String damageCode;
	/*
	 * 出险时间
	 */
	@XStreamAlias("DAMAGEDATE")
	private String damageDate;
	/*
	 * 出险地点
	 */
	@XStreamAlias("DAMAGEADDRESS")
	private String damageAddress;
	/*
	 * 报案时间
	 */
	@XStreamAlias("REPORTTIME")
	private String reportTime;
	/*
	 * 结案时间
	 */
	@XStreamAlias("CLOSEDATE")
	private String closeDate;
	/*
	 * 已决金额
	 */
	@XStreamAlias("SETTLEDAMOUNT")
	private String settledAmount;
	
	/*
	 * 赔付金额
	 */
	@XStreamAlias("PAYMENTAMOUNT")
	private String paymentAmount;
	
	/*
	 * 赔案状态
	 */
	@XStreamAlias("CASESTATE")
	private String caseState;
	
	/*
	 * 案件类型
	 */
	@XStreamAlias("CASETYPE")
	private String caseType;
	/*
	 * 碰撞部位
	 */
	@XStreamAlias("COLLISIONSITE")
	private String collisionSite;

	/*
	 * 收款人信息
	 */
	@XStreamAlias("ACCOUNTLIST")
	private List<AccountVo> accountList;

	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	
	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getBusipolicyNo() {
		return busipolicyNo;
	}

	public void setBusipolicyNo(String busipolicyNo) {
		this.busipolicyNo = busipolicyNo;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getDamageCode() {
		return damageCode;
	}

	public void setDamageCode(String damageCode) {
		this.damageCode = damageCode;
	}

	public String getDamageDate() {
		return damageDate;
	}

	public void setDamageDate(String damageDate) {
		this.damageDate = damageDate;
	}

	public String getDamageAddress() {
		return damageAddress;
	}

	public void setDamageAddress(String damageAddress) {
		this.damageAddress = damageAddress;
	}

	public String getReportTime() {
		return reportTime;
	}

	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}

	public String getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(String closeDate) {
		this.closeDate = closeDate;
	}

	public String getSettledAmount() {
		return settledAmount;
	}

	public void setSettledAmount(String settledAmount) {
		this.settledAmount = settledAmount;
	}

	public String getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(String paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getCaseState() {
		return caseState;
	}

	public void setCaseState(String caseState) {
		this.caseState = caseState;
	}

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	public String getCollisionSite() {
		return collisionSite;
	}

	public void setCollisionSite(String collisionSite) {
		this.collisionSite = collisionSite;
	}

	public List<AccountVo> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<AccountVo> accountList) {
		this.accountList = accountList;
	}





}
