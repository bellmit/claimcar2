package ins.sino.claimcar.policyInfo.register.vo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommercialClaimVo implements Serializable{
	private static final long serialVersionUID = 1L;
	private String InsurerCode;//公司代码
	private String ClaimSequenceNo;//理赔编码
	private String ConfirmSequenceNo;//投保确认码
	private String PolicyNo;//保单号码
	private String NotificationTime;//报案时间
	private String ClaimNotificationNo;//报案号
	private String Reporter;//报案人姓名
	private String LossTime;//出险时间
	private String LossArea;//出险地点
	private String LossDesc;//出险经过
	private String LossCauseCode;//出险原因代码
	private String AccidentLiability;//事故责任划分代码
	private String OptionType;//事故处理方式代码
	private String ClaimRegistrationNo;//立案号
	private String ClaimRegistrationTime;//立案时间
	private String EstimatedLossAmount;//估损金额
	private String ClaimType;//理赔类型代码
	private String ClaimCloseTime;//结案时间
	private String ClaimAmount;//赔款总金额
	private String IsInsured;//是否属于保险责任
	private String AccidentType;//保险事故分类
	private String IsTotalLoss;//是否全损
	private String CityCode;//保单归属地（地市）
	private String CountyCode;//保单归属地（区县）
	private String ReopenCase;//是否重开赔案	
	public String getInsurerCode() {
		return InsurerCode;
	}
	public void setInsurerCode(String insurerCode) {
		InsurerCode = insurerCode;
	}
	public String getClaimSequenceNo() {
		return ClaimSequenceNo;
	}
	public void setClaimSequenceNo(String claimSequenceNo) {
		ClaimSequenceNo = claimSequenceNo;
	}
	public String getConfirmSequenceNo() {
		return ConfirmSequenceNo;
	}
	public void setConfirmSequenceNo(String confirmSequenceNo) {
		ConfirmSequenceNo = confirmSequenceNo;
	}
	public String getPolicyNo() {
		return PolicyNo;
	}
	public void setPolicyNo(String policyNo) {
		PolicyNo = policyNo;
	}
	public String getNotificationTime() {
		return NotificationTime;
	}
	public void setNotificationTime(String notificationTime) {
		NotificationTime = notificationTime;
	}
	public String getClaimNotificationNo() {
		return ClaimNotificationNo;
	}
	public void setClaimNotificationNo(String claimNotificationNo) {
		ClaimNotificationNo = claimNotificationNo;
	}
	public String getReporter() {
		return Reporter;
	}
	public void setReporter(String reporter) {
		Reporter = reporter;
	}
	public String getLossTime() {
		return LossTime;
	}
	public void setLossTime(String lossTime) {
		LossTime = lossTime;
	}
	public String getLossArea() {
		return LossArea;
	}
	public void setLossArea(String lossArea) {
		LossArea = lossArea;
	}
	public String getLossDesc() {
		return LossDesc;
	}
	public void setLossDesc(String lossDesc) {
		LossDesc = lossDesc;
	}
	public String getLossCauseCode() {
		return LossCauseCode;
	}
	public void setLossCauseCode(String lossCauseCode) {
		LossCauseCode = lossCauseCode;
	}
	public String getAccidentLiability() {
		return AccidentLiability;
	}
	public void setAccidentLiability(String accidentLiability) {
		AccidentLiability = accidentLiability;
	}
	public String getOptionType() {
		return OptionType;
	}
	public void setOptionType(String optionType) {
		OptionType = optionType;
	}
	public String getClaimRegistrationNo() {
		return ClaimRegistrationNo;
	}
	public void setClaimRegistrationNo(String claimRegistrationNo) {
		ClaimRegistrationNo = claimRegistrationNo;
	}
	public String getClaimRegistrationTime() {
		return ClaimRegistrationTime;
	}
	public void setClaimRegistrationTime(String claimRegistrationTime) {
		ClaimRegistrationTime = claimRegistrationTime;
	}
	public String getEstimatedLossAmount() {
		return EstimatedLossAmount;
	}
	public void setEstimatedLossAmount(String estimatedLossAmount) {
		EstimatedLossAmount = estimatedLossAmount;
	}
	public String getClaimType() {
		return ClaimType;
	}
	public void setClaimType(String claimType) {
		ClaimType = claimType;
	}
	public String getClaimCloseTime() {
		return ClaimCloseTime;
	}
	public void setClaimCloseTime(String claimCloseTime) {
		ClaimCloseTime = claimCloseTime;
	}
	public String getClaimAmount() {
		return ClaimAmount;
	}
	public void setClaimAmount(String claimAmount) {
		ClaimAmount = claimAmount;
	}
	public String getIsInsured() {
		return IsInsured;
	}
	public void setIsInsured(String isInsured) {
		IsInsured = isInsured;
	}
	public String getAccidentType() {
		return AccidentType;
	}
	public void setAccidentType(String accidentType) {
		AccidentType = accidentType;
	}
	public String getIsTotalLoss() {
		return IsTotalLoss;
	}
	public void setIsTotalLoss(String isTotalLoss) {
		IsTotalLoss = isTotalLoss;
	}
	public String getCityCode() {
		return CityCode;
	}
	public void setCityCode(String cityCode) {
		CityCode = cityCode;
	}
	public String getCountyCode() {
		return CountyCode;
	}
	public void setCountyCode(String countyCode) {
		CountyCode = countyCode;
	}
	public String getReopenCase() {
		return ReopenCase;
	}
	public void setReopenCase(String reopenCase) {
		ReopenCase = reopenCase;
	}
	

}
