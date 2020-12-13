package ins.sino.claimcar.interf.vo;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CommercialInsurance")
public class CommercialInsuranceVo {
  
	/*是否约定驾驶员标志*/
	@XStreamAlias("AgreeDriverFlag")
	private String agreeDriverFlag;
	/*投保日期*/
	@XStreamAlias("BuyInsuranceDate")
	private String buyInsuranceDate;
	/*保单出险记录列表*/
	@XStreamAlias("HistoricalClaims")
	private List<HistoricalClaimVo> historicalClaims;
	/*总保额*/
	@XStreamAlias("InsuranceAmount")
	private String insuranceAmount;
	/*险别*/
	@XStreamAlias("InsuranceCategory")
	private String insuranceCategory;
	/*终保时间*/
	@XStreamAlias("InsuranceEndDate")
	private String insuranceEndDate;
	/*总保费*/
	@XStreamAlias("InsuranceFee")
	private String insuranceFee;
	/*险种详情列表*/
	@XStreamAlias("InsuranceItems")
	private List<InsuranceItemVo> InsuranceItems;
	/*批单信息列表*/
	@XStreamAlias("InsuranceModifications")
	private List<InsuranceModificationVo> insuranceModifications;
	/*保单号*/
	@XStreamAlias("InsuranceNumber")
	private String insuranceNumber;
	/*起保时间*/
	@XStreamAlias("InsuranceStartDate")
	private String insuranceStartDate;
	/*出单日期*/
	@XStreamAlias("IssueDate")
	private String issueDate;
	/*投保人*/
	@XStreamAlias("PolicyHolder")
	private String policyHolder;
	/*签单地点*/
	@XStreamAlias("SignAddress")
	private String signAddress;
	/*签单时间*/
	@XStreamAlias("SignDate")
	private String signDate;
	/*保单特别约定列表*/
	@XStreamAlias("SpecialAgreements")
	private List<SpecialAgreementVo> specialAgreements;
	/*被保险人*/
	@XStreamAlias("TheInsuredName")
	private String theInsuredName;
	public String getAgreeDriverFlag() {
		return agreeDriverFlag;
	}
	public void setAgreeDriverFlag(String agreeDriverFlag) {
		this.agreeDriverFlag = agreeDriverFlag;
	}
	public String getBuyInsuranceDate() {
		return buyInsuranceDate;
	}
	public void setBuyInsuranceDate(String buyInsuranceDate) {
		this.buyInsuranceDate = buyInsuranceDate;
	}
	public List<HistoricalClaimVo> getHistoricalClaims() {
		return historicalClaims;
	}
	public void setHistoricalClaims(List<HistoricalClaimVo> historicalClaims) {
		this.historicalClaims = historicalClaims;
	}
	public String getInsuranceAmount() {
		return insuranceAmount;
	}
	public void setInsuranceAmount(String insuranceAmount) {
		this.insuranceAmount = insuranceAmount;
	}
	public String getInsuranceCategory() {
		return insuranceCategory;
	}
	public void setInsuranceCategory(String insuranceCategory) {
		this.insuranceCategory = insuranceCategory;
	}
	public String getInsuranceEndDate() {
		return insuranceEndDate;
	}
	public void setInsuranceEndDate(String insuranceEndDate) {
		this.insuranceEndDate = insuranceEndDate;
	}
	public String getInsuranceFee() {
		return insuranceFee;
	}
	public void setInsuranceFee(String insuranceFee) {
		this.insuranceFee = insuranceFee;
	}
	public List<InsuranceItemVo> getInsuranceItems() {
		return InsuranceItems;
	}
	public void setInsuranceItems(List<InsuranceItemVo> insuranceItems) {
		InsuranceItems = insuranceItems;
	}
	public List<InsuranceModificationVo> getInsuranceModifications() {
		return insuranceModifications;
	}
	public void setInsuranceModifications(
			List<InsuranceModificationVo> insuranceModifications) {
		this.insuranceModifications = insuranceModifications;
	}
	public String getInsuranceNumber() {
		return insuranceNumber;
	}
	public void setInsuranceNumber(String insuranceNumber) {
		this.insuranceNumber = insuranceNumber;
	}
	public String getInsuranceStartDate() {
		return insuranceStartDate;
	}
	public void setInsuranceStartDate(String insuranceStartDate) {
		this.insuranceStartDate = insuranceStartDate;
	}
	public String getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}
	public String getPolicyHolder() {
		return policyHolder;
	}
	public void setPolicyHolder(String policyHolder) {
		this.policyHolder = policyHolder;
	}
	public String getSignAddress() {
		return signAddress;
	}
	public void setSignAddress(String signAddress) {
		this.signAddress = signAddress;
	}
	public String getSignDate() {
		return signDate;
	}
	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}
	public List<SpecialAgreementVo> getSpecialAgreements() {
		return specialAgreements;
	}
	public void setSpecialAgreements(List<SpecialAgreementVo> specialAgreements) {
		this.specialAgreements = specialAgreements;
	}
	public String getTheInsuredName() {
		return theInsuredName;
	}
	public void setTheInsuredName(String theInsuredName) {
		this.theInsuredName = theInsuredName;
	}
	

}
