package ins.sino.claimcar.ilog.certify;

import ins.sino.claimcar.lossperson.vo.ILPersonnelInfoVo;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author sinosoft
 *
 */
@XStreamAlias("root")
public class ReqRoot {
	@XStreamAlias("registno")
    private String registNo;//报案号
	@XStreamAlias("comCode")
	private String comCode;//机构代码
	@XStreamAlias("lossItemType")
	private String lossItemType;//人员属性
	@XStreamAlias("auditFeeSumAmount")
	private String auditFeeSumAmount;//审核定损金额合计
	@XStreamAlias("validityCarFeeMissionNum")
	private String validityCarFeeMissionNum;//有效车辆定损任务数量
	@XStreamAlias("casualtiesCaseFlag")
	private String casualtiesCaseFlag;//是涉及人伤
	@XStreamAlias("carKindCode")
	private String carKindCode;//承保车辆种类A
	@XStreamAlias("isJQFraud")
	private String isJQFraud;//是否交强拒赔
	@XStreamAlias("isSYFraud")
	private String isSYFraud;//是否商业拒赔
	@XStreamAlias("lawsuitFlag")
	private String lawsuitFlag;//是否诉讼
	@XStreamAlias("subrogationFlag")
	private String subrogationFlag;//是否代位求偿案件
	@XStreamAlias("isPrepaidType")
	private String isPrepaidType;//是否预付类型
	@XStreamAlias("isAdvancesCcase")
	private String isAdvancesCcase;//是否垫付案件
	@XStreamAlias("isAssessmentSurvey")
	private String isAssessmentSurvey;//是否公估查勘
	@XStreamAlias("payeeInfoNum")
	private String payeeInfoNum;//报案号下收款人信息条数
	@XStreamAlias("vehicleCertainAmount")
	private String vehicleCertainAmount;//报案号下收款人信息条数
	@XStreamAlias("pauditAmount")
	private String pauditAmount;//人伤费用审核定损金额
	@XStreamAlias("isReopenClaim")
	private String isReopenClaim;//是否重开赔案
	@XStreamAlias("directFlag")
	private String directFlag;//单证是否齐全
	@XStreamAlias("isAuto")
	private String isAuto;//人伤案件查勘是否费用审核自动通过
	@XStreamAlias("isSpecialOperationLicense")
	private String isSpecialOperationLicense;//是否有特种车操作证
	@XStreamAlias("isBusinessCarCertificate")
	private String isBusinessCarCertificate;//是否有营业车资格证
	@XStreamAlias("useKindCode")
	private String useKindCode;//使用性质
	@XStreamAlias("isSurveyCase")
	private String isSurveyCase;//是否代查勘案件
	@XStreamAlias("surveyFlag")
	private String surveyFlag;//是否调查案件
	@XStreamAlias("isFlagN")
	private String isFlagN;//是否可疑交易
	@XStreamAlias("sysAuthorizationFlag")
	private String sysAuthorizationFlag;//是否系统授权
	@XStreamAlias("Isinvolvedincarlaitp")
	private String isinvolvedincarlaitp;//是否涉及减损金额的录入
	@XStreamAlias("sumAmount")
	private String sumAmount;//总赔款金额
	@XStreamAlias("coinsFlag")
	private String coinsFlag;//共保标志
	@XStreamAlias("accidentResponsibilityList")
	private List<AccidentResponsibilityVo> bilitys;//事故责任比例列表
	@XStreamAlias("personnelInfoList")
	private List<ILPersonnelInfoVo> personnelInfos;//损失项信息列表
	@XStreamAlias("sumProportion")
	private String sumProportion; // 责任比例之和
	
	public String getSumProportion() {
		return sumProportion;
	}
	public void setSumProportion(String sumProportion) {
		this.sumProportion = sumProportion;
	}
	public String getCoinsFlag() {
		return coinsFlag;
	}
	public void setCoinsFlag(String coinsFlag) {
		this.coinsFlag = coinsFlag;
	}
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public String getComCode() {
		return comCode;
	}
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	public String getLossItemType() {
		return lossItemType;
	}
	public void setLossItemType(String lossItemType) {
		this.lossItemType = lossItemType;
	}
	public String getAuditFeeSumAmount() {
		return auditFeeSumAmount;
	}
	public void setAuditFeeSumAmount(String auditFeeSumAmount) {
		this.auditFeeSumAmount = auditFeeSumAmount;
	}
	public String getValidityCarFeeMissionNum() {
		return validityCarFeeMissionNum;
	}
	public void setValidityCarFeeMissionNum(String validityCarFeeMissionNum) {
		this.validityCarFeeMissionNum = validityCarFeeMissionNum;
	}
	public String getCasualtiesCaseFlag() {
		return casualtiesCaseFlag;
	}
	public void setCasualtiesCaseFlag(String casualtiesCaseFlag) {
		this.casualtiesCaseFlag = casualtiesCaseFlag;
	}
	public String getCarKindCode() {
		return carKindCode;
	}
	public void setCarKindCode(String carKindCode) {
		this.carKindCode = carKindCode;
	}
	public String getIsJQFraud() {
		return isJQFraud;
	}
	public void setIsJQFraud(String isJQFraud) {
		this.isJQFraud = isJQFraud;
	}
	public String getIsSYFraud() {
		return isSYFraud;
	}
	public void setIsSYFraud(String isSYFraud) {
		this.isSYFraud = isSYFraud;
	}
	public String getLawsuitFlag() {
		return lawsuitFlag;
	}
	public void setLawsuitFlag(String lawsuitFlag) {
		this.lawsuitFlag = lawsuitFlag;
	}
	public String getSubrogationFlag() {
		return subrogationFlag;
	}
	public void setSubrogationFlag(String subrogationFlag) {
		this.subrogationFlag = subrogationFlag;
	}
	public String getIsPrepaidType() {
		return isPrepaidType;
	}
	public void setIsPrepaidType(String isPrepaidType) {
		this.isPrepaidType = isPrepaidType;
	}
	public String getIsAdvancesCcase() {
		return isAdvancesCcase;
	}
	public void setIsAdvancesCcase(String isAdvancesCcase) {
		this.isAdvancesCcase = isAdvancesCcase;
	}
	public String getIsAssessmentSurvey() {
		return isAssessmentSurvey;
	}
	public void setIsAssessmentSurvey(String isAssessmentSurvey) {
		this.isAssessmentSurvey = isAssessmentSurvey;
	}
	public String getPayeeInfoNum() {
		return payeeInfoNum;
	}
	public void setPayeeInfoNum(String payeeInfoNum) {
		this.payeeInfoNum = payeeInfoNum;
	}
	public String getVehicleCertainAmount() {
		return vehicleCertainAmount;
	}
	public void setVehicleCertainAmount(String vehicleCertainAmount) {
		this.vehicleCertainAmount = vehicleCertainAmount;
	}
	public String getPauditAmount() {
		return pauditAmount;
	}
	public void setPauditAmount(String pauditAmount) {
		this.pauditAmount = pauditAmount;
	}
	public String getIsReopenClaim() {
		return isReopenClaim;
	}
	public void setIsReopenClaim(String isReopenClaim) {
		this.isReopenClaim = isReopenClaim;
	}
	public String getDirectFlag() {
		return directFlag;
	}
	public void setDirectFlag(String directFlag) {
		this.directFlag = directFlag;
	}
	public String getIsAuto() {
		return isAuto;
	}
	public void setIsAuto(String isAuto) {
		this.isAuto = isAuto;
	}
	public String getIsSpecialOperationLicense() {
		return isSpecialOperationLicense;
	}
	public void setIsSpecialOperationLicense(String isSpecialOperationLicense) {
		this.isSpecialOperationLicense = isSpecialOperationLicense;
	}
	
	public String getIsBusinessCarCertificate() {
		return isBusinessCarCertificate;
	}
	public void setIsBusinessCarCertificate(String isBusinessCarCertificate) {
		this.isBusinessCarCertificate = isBusinessCarCertificate;
	}
	public String getUseKindCode() {
		return useKindCode;
	}
	public void setUseKindCode(String useKindCode) {
		this.useKindCode = useKindCode;
	}
	public String getIsSurveyCase() {
		return isSurveyCase;
	}
	public void setIsSurveyCase(String isSurveyCase) {
		this.isSurveyCase = isSurveyCase;
	}
	
	public String getSurveyFlag() {
		return surveyFlag;
	}
	
	public void setSurveyFlag(String surveyFlag) {
		this.surveyFlag = surveyFlag;
	}
	
	public String getIsFlagN() {
		return isFlagN;
	}
	
	public void setIsFlagN(String isFlagN) {
		this.isFlagN = isFlagN;
	}
	
	public String getSysAuthorizationFlag() {
		return sysAuthorizationFlag;
	}
	
	
	public void setSysAuthorizationFlag(String sysAuthorizationFlag) {
		this.sysAuthorizationFlag = sysAuthorizationFlag;
	}
	
	
	public String getIsinvolvedincarlaitp() {
		return isinvolvedincarlaitp;
	}
	public void setIsinvolvedincarlaitp(String isinvolvedincarlaitp) {
		this.isinvolvedincarlaitp = isinvolvedincarlaitp;
	}
	public List<AccidentResponsibilityVo> getBilitys() {
		return bilitys;
	}
	public void setBilitys(List<AccidentResponsibilityVo> bilitys) {
		this.bilitys = bilitys;
	}
	
	
	public List<ILPersonnelInfoVo> getPersonnelInfos() {
		return personnelInfos;
	}
	
	public void setPersonnelInfos(List<ILPersonnelInfoVo> personnelInfos) {
		this.personnelInfos = personnelInfos;
	}
	public String getSumAmount() {
		return sumAmount;
	}
	public void setSumAmount(String sumAmount) {
		this.sumAmount = sumAmount;
	}
	
	
	
}
