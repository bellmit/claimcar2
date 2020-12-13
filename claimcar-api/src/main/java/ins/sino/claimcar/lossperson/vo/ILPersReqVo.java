package ins.sino.claimcar.lossperson.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("root")
public class ILPersReqVo  implements Serializable {

	private static final long serialVersionUID = 8423652723600188374L;
	
	@XStreamAlias("registno")
	private String registNo;  //报案号 
	@XStreamAlias("currentNodeNo")
	private int currentNodeNo;  //当前审核级别 
	@XStreamAlias("casualtyType")
	private String casualtyType;  //
	@XStreamAlias("operateType")
	private String operateType;  //
	@XStreamAlias("comCode")
	private String comCode;  //
	@XStreamAlias("requestSource")
	private String requestSource;  //
	@XStreamAlias("companyFlag")
	private String companyFlag;  //
	@XStreamAlias("disclaimerFlag")
	private String disclaimerFlag;  //
	@XStreamAlias("isReopenClaim")
	private String isReopenClaim;  //
	@XStreamAlias("employeeId")
	private String employeeId;  //
	@XStreamAlias("evaluateLossAmount")
	private BigDecimal evaluateLossAmount;  //
	@XStreamAlias("sureLossAmount")
	private BigDecimal sureLossAmount;  //
	@XStreamAlias("reconcileFlag")
	private String reconcileFlag;  //
	@XStreamAlias("JQDamagTime")
	private int JQDamagTime;  //
	@XStreamAlias("SYDamagTime")
	private int SYDamagTime;  //
	@XStreamAlias("validityCarFeeMissionNum")
	private int validityCarFeeMissionNum;  //
	@XStreamAlias("peoHurtImageNum")
	private int peoHurtImageNum;  //
	@XStreamAlias("conciliationImageNum")
	private int conciliationImageNum;  //
	@XStreamAlias("sceneImageNum")
	private int sceneImageNum;  //
	@XStreamAlias("surveyImage")
	private int surveyImage;  //
	@XStreamAlias("responsibilityNum")
	private int responsibilityNum;  //
	@XStreamAlias("diagnosticCertificateNum")
	private int diagnosticCertificateNum;  //
	@XStreamAlias("reimbursementNum")
	private int reimbursementNum;  //
	@XStreamAlias("feeListImage")
	private int feeListImage;  //
	@XStreamAlias("validityCasualtiesNum")
	private int validityCasualtiesNum;  //
	@XStreamAlias("surveySubDate")
	private Date surveySubDate;  //
	@XStreamAlias("surveySubHour")
	private int surveySubHour;  //
	@XStreamAlias("surveySubMinute")
	private int surveySubMinute;  //
	@XStreamAlias("reportDate")
	private Date reportDate;  //
	@XStreamAlias("reportHour")
	private int reportHour;
	@XStreamAlias("reportMinute")
	private int reportMinute;
	@XStreamAlias("feeCheckFlag")
	private String feeCheckFlag;
	@XStreamAlias("caseProcessType")
	private String caseProcessType;
	@XStreamAlias("numAccess")
	private String numAccess;
	@XStreamAlias("sumAmount")
	private BigDecimal sumAmount;
	@XStreamAlias("surveyFlag")
	private String surveyFlag;
	@XStreamAlias("isFlagN")
	private String isFlagN ;
	@XStreamAlias("majorcaseFlag")
	private String  majorcaseFlag;
	@XStreamAlias("sumPaidFee")
	private BigDecimal  sumPaidFee;//人伤环节录入的直接理赔费用
	@XStreamAlias("isWhethertheloss")
	private String  isWhethertheloss;
	@XStreamAlias("coinsFlag")
	private String  coinsFlag;
	@XStreamAlias("personnelInfoList")
	private List<ILPersonnelInfoVo> personnelInfoList;
	@XStreamAlias("personnelbasicList")
	private List<ILPersonnelBasic> personelBasic;
	@XStreamAlias("existHeadOffice")
	private String existHeadOffice;
	
	
	
	public String getExistHeadOffice() {
		return existHeadOffice;
	}
	
	public void setExistHeadOffice(String existHeadOffice) {
		this.existHeadOffice = existHeadOffice;
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
	public int getCurrentNodeNo() {
		return currentNodeNo;
	}
	public void setCurrentNodeNo(int currentNodeNo) {
		this.currentNodeNo = currentNodeNo;
	}
	public String getCasualtyType() {
		return casualtyType;
	}
	public void setCasualtyType(String casualtyType) {
		this.casualtyType = casualtyType;
	}
	public String getOperateType() {
		return operateType;
	}
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}
	public String getComCode() {
		return comCode;
	}
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	public String getRequestSource() {
		return requestSource;
	}
	public void setRequestSource(String requestSource) {
		this.requestSource = requestSource;
	}
	public String getCompanyFlag() {
		return companyFlag;
	}
	public void setCompanyFlag(String companyFlag) {
		this.companyFlag = companyFlag;
	}
	public String getDisclaimerFlag() {
		return disclaimerFlag;
	}
	public void setDisclaimerFlag(String disclaimerFlag) {
		this.disclaimerFlag = disclaimerFlag;
	}
	public String getIsReopenClaim() {
		return isReopenClaim;
	}
	public void setIsReopenClaim(String isReopenClaim) {
		this.isReopenClaim = isReopenClaim;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public BigDecimal getEvaluateLossAmount() {
		return evaluateLossAmount;
	}
	public void setEvaluateLossAmount(BigDecimal evaluateLossAmount) {
		this.evaluateLossAmount = evaluateLossAmount;
	}
	public BigDecimal getSureLossAmount() {
		return sureLossAmount;
	}
	public void setSureLossAmount(BigDecimal sureLossAmount) {
		this.sureLossAmount = sureLossAmount;
	}
	public String getReconcileFlag() {
		return reconcileFlag;
	}
	public void setReconcileFlag(String reconcileFlag) {
		this.reconcileFlag = reconcileFlag;
	}
	public int getJQDamagTime() {
		return JQDamagTime;
	}
	public void setJQDamagTime(int jQDamagTime) {
		JQDamagTime = jQDamagTime;
	}
	public int getSYDamagTime() {
		return SYDamagTime;
	}
	public void setSYDamagTime(int sYDamagTime) {
		SYDamagTime = sYDamagTime;
	}
	public int getValidityCarFeeMissionNum() {
		return validityCarFeeMissionNum;
	}
	public void setValidityCarFeeMissionNum(int validityCarFeeMissionNum) {
		this.validityCarFeeMissionNum = validityCarFeeMissionNum;
	}
	public int getPeoHurtImageNum() {
		return peoHurtImageNum;
	}
	public void setPeoHurtImageNum(int peoHurtImageNum) {
		this.peoHurtImageNum = peoHurtImageNum;
	}
	public int getConciliationImageNum() {
		return conciliationImageNum;
	}
	public void setConciliationImageNum(int conciliationImageNum) {
		this.conciliationImageNum = conciliationImageNum;
	}
	public int getSceneImageNum() {
		return sceneImageNum;
	}
	public void setSceneImageNum(int sceneImageNum) {
		this.sceneImageNum = sceneImageNum;
	}
	public int getSurveyImage() {
		return surveyImage;
	}
	public void setSurveyImage(int surveyImage) {
		this.surveyImage = surveyImage;
	}
	public int getResponsibilityNum() {
		return responsibilityNum;
	}
	public void setResponsibilityNum(int responsibilityNum) {
		this.responsibilityNum = responsibilityNum;
	}
	public int getDiagnosticCertificateNum() {
		return diagnosticCertificateNum;
	}
	public void setDiagnosticCertificateNum(int diagnosticCertificateNum) {
		this.diagnosticCertificateNum = diagnosticCertificateNum;
	}
	public int getReimbursementNum() {
		return reimbursementNum;
	}
	public void setReimbursementNum(int reimbursementNum) {
		this.reimbursementNum = reimbursementNum;
	}
	public int getFeeListImage() {
		return feeListImage;
	}
	public void setFeeListImage(int feeListImage) {
		this.feeListImage = feeListImage;
	}
	public int getValidityCasualtiesNum() {
		return validityCasualtiesNum;
	}
	public void setValidityCasualtiesNum(int validityCasualtiesNum) {
		this.validityCasualtiesNum = validityCasualtiesNum;
	}
	public Date getSurveySubDate() {
		return surveySubDate;
	}
	public void setSurveySubDate(Date surveySubDate) {
		this.surveySubDate = surveySubDate;
	}
	public int getSurveySubHour() {
		return surveySubHour;
	}
	public void setSurveySubHour(int surveySubHour) {
		this.surveySubHour = surveySubHour;
	}
	public int getSurveySubMinute() {
		return surveySubMinute;
	}
	public void setSurveySubMinute(int surveySubMinute) {
		this.surveySubMinute = surveySubMinute;
	}
	public Date getReportDate() {
		return reportDate;
	}
	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}
	public int getReportHour() {
		return reportHour;
	}
	public void setReportHour(int reportHour) {
		this.reportHour = reportHour;
	}
	public int getReportMinute() {
		return reportMinute;
	}
	public void setReportMinute(int reportMinute) {
		this.reportMinute = reportMinute;
	}
	public String getFeeCheckFlag() {
		return feeCheckFlag;
	}
	public void setFeeCheckFlag(String feeCheckFlag) {
		this.feeCheckFlag = feeCheckFlag;
	}
	public String getCaseProcessType() {
		return caseProcessType;
	}
	public void setCaseProcessType(String caseProcessType) {
		this.caseProcessType = caseProcessType;
	}
	public String getNumAccess() {
		return numAccess;
	}
	public void setNumAccess(String numAccess) {
		this.numAccess = numAccess;
	}
	public BigDecimal getSumAmount() {
		return sumAmount;
	}
	public void setSumAmount(BigDecimal sumAmount) {
		this.sumAmount = sumAmount;
	}
	public List<ILPersonnelInfoVo> getPersonnelInfoList() {
		return personnelInfoList;
	}
	public void setPersonnelInfoList(List<ILPersonnelInfoVo> personnelInfoList) {
		this.personnelInfoList = personnelInfoList;
	}
	public List<ILPersonnelBasic> getPersonelBasic() {
		return personelBasic;
	}
	public void setPersonelBasic(List<ILPersonnelBasic> personelBasic) {
		this.personelBasic = personelBasic;
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
	public String getMajorcaseFlag() {
		return majorcaseFlag;
	}
	public void setMajorcaseFlag(String majorcaseFlag) {
		this.majorcaseFlag = majorcaseFlag;
	}
	public BigDecimal getSumPaidFee() {
		return sumPaidFee;
	}
	public void setSumPaidFee(BigDecimal sumPaidFee) {
		this.sumPaidFee = sumPaidFee;
	}
	public String getIsWhethertheloss() {
		return isWhethertheloss;
	}
	public void setIsWhethertheloss(String isWhethertheloss) {
		this.isWhethertheloss = isWhethertheloss;
	}
	
	
	
}
