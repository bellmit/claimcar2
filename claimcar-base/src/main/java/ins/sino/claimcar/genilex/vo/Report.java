package ins.sino.claimcar.genilex.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Report")
public class Report implements Serializable {

	/** 理赔报案信息 */
	private static final long serialVersionUID = 6606450331852430142L;
	
	@XStreamAlias("ReportNo")
	private String reportNo;  //报案号
	
    @XStreamAlias("ReportTime")
    private String reportTime;  //报案时间
    
    @XStreamAlias("ReporterCode")
    private String reporterCode;  //报案人代码
    
    @XStreamAlias("Reporter")
    private String reporter;  //报案人姓名
    
    @XStreamAlias("SubrogationFlag")
    private String subrogationFlag;  //是否要求代位
    
    @XStreamAlias("ReportTel")
    private String reportTel;  //报案电话
    
    @XStreamAlias("MessageTel")
    private String messageTel;  //短信通知电话
    
    @XStreamAlias("ReporterMode")
    private String reporterMode;  //报案方式
    
    @XStreamAlias("RunningBegin")
    private String runningBegin;  //行驶起点（或方位）
	   
    @XStreamAlias("RunningEnd")
    private String runningEnd;  //行驶终点（或方位）
    
    @XStreamAlias("AskingforRescueInd")
    private String askingforRescueInd;  //请求施救标志
    
    @XStreamAlias("CatastropheLossInd")
    private String catastropheLossInd;  //重大赔案标志
    
    @XStreamAlias("SpecialCaseInd")
    private String specialCaseInd;  //异常案件标志
    
    @XStreamAlias("LaterReportInd")
    private String LaterReportInd;  //晚报案标志
    
    @XStreamAlias("UnlocalInd")
    private String unlocalInd;  //异地出险标志
    
    @XStreamAlias("CurrencyCode")
    private String currencyCode;  //币种
    
    @XStreamAlias("ProbableIncurredAmt")
    private String probableIncurredAmt;  //报损金额/估损金额
    
    @XStreamAlias("ReportedDeptCode")
    private String reportedDeptCode;  //受理机构
    
    @XStreamAlias("ReportedBy")
    private String reportedBy;  //受理人代码
    
    @XStreamAlias("HandleTime")
    private String handleTime;  //受理时间
    
    @XStreamAlias("InsuredTel")
    private String insuredTel;  //被保人电话
    
    @XStreamAlias("LossTypeInd1")
    private String lossTypeInd1;  //是否有标的车损
    
    @XStreamAlias("LossTypeInd2")
    private String lossTypeInd2;  //是否有三者车损
    
    @XStreamAlias("LossTypeInd3")
    private String lossTypeInd3;  //是否有物损
    
    @XStreamAlias("LossTypeInd4")
    private String lossTypeInd4;  //是否有人伤
    
    @XStreamAlias("TargetVehicleNumber")
    private String targetVehicleNumber;  //本车车损数量
    
    @XStreamAlias("ThirdVehNumber")
    private String thirdVehNumber;  //三者车损数量
    
    @XStreamAlias("InjuredNumber")
    private String injuredNumber;  //本车人伤数量
    
    @XStreamAlias("ThirdInjurredNumber")
    private String thirdInjurredNumber;  //三者人伤数量
    
    @XStreamAlias("PropertyLossNumber")
    private String propertyLossNumber;  //本车物损数量
    
    @XStreamAlias("ThirdPropertyLossNumber")
    private String thirdPropertyLossNumber;  //三者物损数量
    
    @XStreamAlias("OthersLossNumber")
    private String othersLossNumber;  //其它损失数量
    
    @XStreamAlias("PropertyLossDesc")
    private String propertyLossDesc;  //本车物损描述
    
    @XStreamAlias("ThirdPropertyLossDesc")
    private String thirdPropertyLossDesc;  //三者物损描述
    
    @XStreamAlias("OthersLossDesc")
    private String othersLossDesc;  //其它损失描述
    
    @XStreamAlias("PolicyNo")
    private String policyNo;  //保单号
    
    @XStreamAlias("InsrntCnm")
    private String insrntCnm;  //被保险人名称
    
    @XStreamAlias("LicenceNo")
    private String licenceNo;  //车牌号
    
    @XStreamAlias("BrandName")
    private String brandName;  //车型(厂牌号)
    
    @XStreamAlias("EngineNo")
    private String engineNo;  //发动机号
    
    @XStreamAlias("FrameNo")
    private String frameNo;  //车架号
    
    @XStreamAlias("CaseFlag")
    private String caseFlag;  //案件类型标志位
    
    @XStreamAlias("SpotOutInd")
    private String spotOutInd;  //派工是否是非出现场标志
    
    @XStreamAlias("SpotOutReasion")
    private String spotOutReasion;  //派工非出现场原因
    
    @XStreamAlias("SelfSurveyType")
    private String selfSurveyType;  //自助查勘类型
    
    @XStreamAlias("DriverCode")
    private String driverCode;  //司机代码
    
    @XStreamAlias("DriverName")
    private String driverName;  //司机姓名
    
    @XStreamAlias("Remark")
    private String remark;  //备注
    
    @XStreamAlias("CreateBy")
    private String createBy;  //创建者
    
    @XStreamAlias("CreateTime")
    private String createTime;  //创建日期
    
    @XStreamAlias("UpdateBy")
    private String updateBy;  //更新者
    
    @XStreamAlias("UpdateTime")
    private String updateTime;  //更新日期
    
    @XStreamAlias("Incidents")
    private List<Incident> incidentsList;  //事故信息
    
    @XStreamAlias("ReportPerson")
    private ReportPerson reportPerson;  //报案人信息
    
    @XStreamAlias("Driver")
    private Driver driver;  //出险驾驶员信息
    
    @XStreamAlias("ClaimReportLinkers")
    private List<ClaimReportLinker> claimReportLinker;  //报案联系人信息
    
    @XStreamAlias("ClaimSuccors")
    private List<ClaimSuccor> claimSuccor;  //车辆救援信息列表

    
    public String getReportNo() {
        return reportNo;
    }

    
    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    
    public String getReportTime() {
        return reportTime;
    }

    
    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    
    public String getReporterCode() {
        return reporterCode;
    }

    
    public void setReporterCode(String reporterCode) {
        this.reporterCode = reporterCode;
    }

    
    public String getReporter() {
        return reporter;
    }

    
    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    
    public String getSubrogationFlag() {
        return subrogationFlag;
    }

    
    public void setSubrogationFlag(String subrogationFlag) {
        this.subrogationFlag = subrogationFlag;
    }

    
    public String getReportTel() {
        return reportTel;
    }

    
    public void setReportTel(String reportTel) {
        this.reportTel = reportTel;
    }

    
    public String getMessageTel() {
        return messageTel;
    }

    
    public void setMessageTel(String messageTel) {
        this.messageTel = messageTel;
    }

    
    public String getReporterMode() {
        return reporterMode;
    }

    
    public void setReporterMode(String reporterMode) {
        this.reporterMode = reporterMode;
    }

    
    public String getRunningBegin() {
        return runningBegin;
    }

    
    public void setRunningBegin(String runningBegin) {
        this.runningBegin = runningBegin;
    }

    
    public String getRunningEnd() {
        return runningEnd;
    }

    
    public void setRunningEnd(String runningEnd) {
        this.runningEnd = runningEnd;
    }

    
    public String getAskingforRescueInd() {
        return askingforRescueInd;
    }

    
    public void setAskingforRescueInd(String askingforRescueInd) {
        this.askingforRescueInd = askingforRescueInd;
    }

    
    public String getCatastropheLossInd() {
        return catastropheLossInd;
    }

    
    public void setCatastropheLossInd(String catastropheLossInd) {
        this.catastropheLossInd = catastropheLossInd;
    }

    
    public String getSpecialCaseInd() {
        return specialCaseInd;
    }

    
    public void setSpecialCaseInd(String specialCaseInd) {
        this.specialCaseInd = specialCaseInd;
    }

    
    public String getLaterReportInd() {
        return LaterReportInd;
    }

    
    public void setLaterReportInd(String laterReportInd) {
        LaterReportInd = laterReportInd;
    }

    
    public String getUnlocalInd() {
        return unlocalInd;
    }

    
    public void setUnlocalInd(String unlocalInd) {
        this.unlocalInd = unlocalInd;
    }

    
    public String getCurrencyCode() {
        return currencyCode;
    }

    
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    
    public String getProbableIncurredAmt() {
        return probableIncurredAmt;
    }

    
    public void setProbableIncurredAmt(String probableIncurredAmt) {
        this.probableIncurredAmt = probableIncurredAmt;
    }

    
    public String getReportedDeptCode() {
        return reportedDeptCode;
    }

    
    public void setReportedDeptCode(String reportedDeptCode) {
        this.reportedDeptCode = reportedDeptCode;
    }

    
    public String getReportedBy() {
        return reportedBy;
    }

    
    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    
    public String getHandleTime() {
        return handleTime;
    }

    
    public void setHandleTime(String handleTime) {
        this.handleTime = handleTime;
    }

    
    public String getInsuredTel() {
        return insuredTel;
    }

    
    public void setInsuredTel(String insuredTel) {
        this.insuredTel = insuredTel;
    }

    
    public String getLossTypeInd1() {
        return lossTypeInd1;
    }

    
    public void setLossTypeInd1(String lossTypeInd1) {
        this.lossTypeInd1 = lossTypeInd1;
    }

    
    public String getLossTypeInd2() {
        return lossTypeInd2;
    }

    
    public void setLossTypeInd2(String lossTypeInd2) {
        this.lossTypeInd2 = lossTypeInd2;
    }

    
    public String getLossTypeInd3() {
        return lossTypeInd3;
    }

    
    public void setLossTypeInd3(String lossTypeInd3) {
        this.lossTypeInd3 = lossTypeInd3;
    }

    
    public String getLossTypeInd4() {
        return lossTypeInd4;
    }

    
    public void setLossTypeInd4(String lossTypeInd4) {
        this.lossTypeInd4 = lossTypeInd4;
    }

    
    public String getTargetVehicleNumber() {
        return targetVehicleNumber;
    }

    
    public void setTargetVehicleNumber(String targetVehicleNumber) {
        this.targetVehicleNumber = targetVehicleNumber;
    }

    
    public String getThirdVehNumber() {
        return thirdVehNumber;
    }

    
    public void setThirdVehNumber(String thirdVehNumber) {
        this.thirdVehNumber = thirdVehNumber;
    }

    
    public String getInjuredNumber() {
        return injuredNumber;
    }

    
    public void setInjuredNumber(String injuredNumber) {
        this.injuredNumber = injuredNumber;
    }

    
    public String getThirdInjurredNumber() {
        return thirdInjurredNumber;
    }

    
    public void setThirdInjurredNumber(String thirdInjurredNumber) {
        this.thirdInjurredNumber = thirdInjurredNumber;
    }

    
    public String getThirdPropertyLossNumber() {
        return thirdPropertyLossNumber;
    }

    
    public void setThirdPropertyLossNumber(String thirdPropertyLossNumber) {
        this.thirdPropertyLossNumber = thirdPropertyLossNumber;
    }

    
    public String getOthersLossNumber() {
        return othersLossNumber;
    }

    
    public void setOthersLossNumber(String othersLossNumber) {
        this.othersLossNumber = othersLossNumber;
    }

    
    public String getPropertyLossDesc() {
        return propertyLossDesc;
    }

    
    public void setPropertyLossDesc(String propertyLossDesc) {
        this.propertyLossDesc = propertyLossDesc;
    }

    
    public String getThirdPropertyLossDesc() {
        return thirdPropertyLossDesc;
    }

    
    public void setThirdPropertyLossDesc(String thirdPropertyLossDesc) {
        this.thirdPropertyLossDesc = thirdPropertyLossDesc;
    }

    
    public String getOthersLossDesc() {
        return othersLossDesc;
    }

    
    public void setOthersLossDesc(String othersLossDesc) {
        this.othersLossDesc = othersLossDesc;
    }

    
    public String getPolicyNo() {
        return policyNo;
    }

    
    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    
    public String getInsrntCnm() {
        return insrntCnm;
    }

    
    public void setInsrntCnm(String insrntCnm) {
        this.insrntCnm = insrntCnm;
    }

    
    public String getLicenceNo() {
        return licenceNo;
    }

    
    public void setLicenceNo(String licenceNo) {
        this.licenceNo = licenceNo;
    }

    
    public String getBrandName() {
        return brandName;
    }

    
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    
    public String getEngineNo() {
        return engineNo;
    }

    
    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    
    public String getFrameNo() {
        return frameNo;
    }

    
    public void setFrameNo(String frameNo) {
        this.frameNo = frameNo;
    }

    
    public String getCaseFlag() {
        return caseFlag;
    }

    
    public void setCaseFlag(String caseFlag) {
        this.caseFlag = caseFlag;
    }

    
    public String getSpotOutInd() {
        return spotOutInd;
    }

    
    public void setSpotOutInd(String spotOutInd) {
        this.spotOutInd = spotOutInd;
    }

    
    public String getSpotOutReasion() {
        return spotOutReasion;
    }

    
    public void setSpotOutReasion(String spotOutReasion) {
        this.spotOutReasion = spotOutReasion;
    }

    
    public String getSelfSurveyType() {
        return selfSurveyType;
    }

    
    public void setSelfSurveyType(String selfSurveyType) {
        this.selfSurveyType = selfSurveyType;
    }

    
    public String getDriverCode() {
        return driverCode;
    }

    
    public void setDriverCode(String driverCode) {
        this.driverCode = driverCode;
    }

    
    public String getDriverName() {
        return driverName;
    }

    
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    
    public String getRemark() {
        return remark;
    }

    
    public void setRemark(String remark) {
        this.remark = remark;
    }

    
    public String getCreateBy() {
        return createBy;
    }

    
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    
    public String getCreateTime() {
        return createTime;
    }

    
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    
    public String getUpdateBy() {
        return updateBy;
    }

    
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    
    public String getUpdateTime() {
        return updateTime;
    }

    
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    
    public List<Incident> getIncidentsList() {
        return incidentsList;
    }

    
    public void setIncidentsList(List<Incident> incidentsList) {
        this.incidentsList = incidentsList;
    }

    
    public ReportPerson getReportPerson() {
        return reportPerson;
    }

    
    public void setReportPerson(ReportPerson reportPerson) {
        this.reportPerson = reportPerson;
    }

    
    public Driver getDriver() {
        return driver;
    }

    
    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    
    public List<ClaimReportLinker> getClaimReportLinker() {
        return claimReportLinker;
    }

    
    public void setClaimReportLinker(List<ClaimReportLinker> claimReportLinker) {
        this.claimReportLinker = claimReportLinker;
    }

    
    public List<ClaimSuccor> getClaimSuccor() {
        return claimSuccor;
    }

    
    public void setClaimSuccor(List<ClaimSuccor> claimSuccor) {
        this.claimSuccor = claimSuccor;
    }


    
    public String getPropertyLossNumber() {
        return propertyLossNumber;
    }


    
    public void setPropertyLossNumber(String propertyLossNumber) {
        this.propertyLossNumber = propertyLossNumber;
    }
    
    
}
