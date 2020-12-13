package ins.sino.claimcar.genilex.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Policy")
public class Policy implements Serializable {

	/** 保单信息 */
	private static final long serialVersionUID = 6606450331852430142L;
	
	@XStreamAlias("PolicyNo")
	private String policyNo;  //保单号
	
    @XStreamAlias("RelationWithInsured")
    private String relationWithInsured;  //投保人和被保人关系
    
    @XStreamAlias("PolicyType")
    private String policyType;  //保单类型
    
    @XStreamAlias("Reporter")
    private String reporter;  //报案人姓名
    
    @XStreamAlias("EdrPrjNo")
    private String edrPrjNo;  //批改期次
    
    @XStreamAlias("GroupFlag")
    private String groupFlag;  //团单标志
    
    @XStreamAlias("LastPolicyNo")
    private String lastPolicyNo;  //上年投保保单号
    
    @XStreamAlias("ConfirmSequenceNo")
    private String confirmSequenceNo;  //平台返回的投保确认码
    
    @XStreamAlias("ApplicationFormNo")
    private String applicationFormNo;  //投保单号
	   
    @XStreamAlias("BillDate")
    private String billDate;  //签单日期
    
    @XStreamAlias("EffectiveDate")
    private String effectiveDate;  //起保日期
    
    @XStreamAlias("ExpireDate")
    private String expireDate;  //终保日期
    
    @XStreamAlias("PeriodDesc")
    private String periodDesc;  //期限描述信息
    
    @XStreamAlias("SpecialAgreementDesc")
    private String specialAgreementDesc;  //特别约定
    
    @XStreamAlias("Agent")
    private String agent;  //经办人
    
    @XStreamAlias("Operator")
    private String operator;  //操作员
    
    @XStreamAlias("CurrencyCode")
    private String currencyCode;  //币种类别代码
    
    @XStreamAlias("TotalBasicPremium")
    private String totalBasicPremium;  //总基础(表定)保费
    
    @XStreamAlias("TotalStandardPremium")
    private String totalStandardPremium;  //总标准(应缴)保费
    
    @XStreamAlias("TotalPremium")
    private String totalPremium;  //总签单(实收)保费
    
    @XStreamAlias("AreaFlag")
    private String areaFlag;  //地区标识
    
    @XStreamAlias("ChannelCode")
    private String channelCode;  //销售渠道代码
    
    @XStreamAlias("ByBusinessAgent")
    private String byBusinessAgent;  //兼业代理渠道代码
    
    @XStreamAlias("SubordinateCode")
    private String subordinateCode;  //承保机构码
    
    @XStreamAlias("PolicyStatus")
    private String policyStatus;  //保单状态
    
    @XStreamAlias("PayType")
    private String payType;  //缴费方式
    
    @XStreamAlias("ReinsrcFlg")
    private String reinsrcFlg;  //分保标志
    
    @XStreamAlias("ProductType")
    private String productType;  //产品性质代码
    
    @XStreamAlias("PlanCode")
    private String planCode;  //产品代码
    
    @XStreamAlias("Sttl")
    private String sttl;  //争议处理方式
    
    @XStreamAlias("DrvRate")
    private String drvRate;  //指定驾驶员
    
    @XStreamAlias("SpecialFlag")
    private String specialFlag;  //特殊标识
    
    @XStreamAlias("UnderwritingType")
    private String underwritingType;  //核保结论
    
    @XStreamAlias("UnderwritingMemo")
    private String underwritingMemo;  //核保结论描述
    
    @XStreamAlias("UnderwritingEmpno")
    private String underwritingEmpno;  //核保人员编码
    
    @XStreamAlias("UnderwritingDate")
    private String underwritingDate;  //核保日期
    
    @XStreamAlias("EdrPrjDate")
    private String edrPrjDate;  //批改日期
    
    @XStreamAlias("CancleDate")
    private String cancleDate;  //实际终止日期
    
    @XStreamAlias("CancleReason")
    private String cancleReason;  //终止原因
    
    @XStreamAlias("CancleReasonMemo")
    private String cancleReasonMemo;  //终止原因描述
    
    @XStreamAlias("Remark")
    private String remark;  //备注
    
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
    
    @XStreamAlias("CreateBy")
    private String createBy;  //创建者
    
    @XStreamAlias("CreateTime")
    private String createTime;  //创建日期
    
    @XStreamAlias("UpdateBy")
    private String updateBy;  //更新者
    
    @XStreamAlias("UpdateTime")
    private String updateTime;  //更新日期
    
    @XStreamAlias("Applicants")
    private List<Applicant> applicants;  //投保人信息
    
    @XStreamAlias("Insureds")
    private List<Insured> insureds;  //被保险人信息列表
    
    @XStreamAlias("VehicleOwner")
    private VehicleOwner vehicleOwner;  //车主信息

    @XStreamAlias("CoverageItems")//承保险种信息列表
    private List<CoverageItem> coverageItems;  //车主信息
    
    @XStreamAlias("Endorsements")
    private List<Endorsement> endorsements;  //批改信息列表

    @XStreamAlias("PolicyAgent")
    private PolicyAgent policyAgent;  //代理人信息
    
    @XStreamAlias("Vehicles")
    private List<Vehicle> vehicles;  //车辆信息列表
    
    @XStreamAlias("SpecifiedDrivers")
    private List<SpecifiedDriver> specifiedDrivers;  //车辆信息列表

    
    public String getPolicyNo() {
        return policyNo;
    }

    
    public void setPolicyNo(String policyNo) {
        this.policyNo = policyNo;
    }

    
    public String getRelationWithInsured() {
        return relationWithInsured;
    }

    
    public void setRelationWithInsured(String relationWithInsured) {
        this.relationWithInsured = relationWithInsured;
    }

    
    public String getPolicyType() {
        return policyType;
    }

    
    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    
    public String getReporter() {
        return reporter;
    }

    
    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    
    public String getEdrPrjNo() {
        return edrPrjNo;
    }

    
    public void setEdrPrjNo(String edrPrjNo) {
        this.edrPrjNo = edrPrjNo;
    }

    
    public String getGroupFlag() {
        return groupFlag;
    }

    
    public void setGroupFlag(String groupFlag) {
        this.groupFlag = groupFlag;
    }

    
    public String getLastPolicyNo() {
        return lastPolicyNo;
    }

    
    public void setLastPolicyNo(String lastPolicyNo) {
        this.lastPolicyNo = lastPolicyNo;
    }

    
    public String getConfirmSequenceNo() {
        return confirmSequenceNo;
    }

    
    public void setConfirmSequenceNo(String confirmSequenceNo) {
        this.confirmSequenceNo = confirmSequenceNo;
    }

    
    public String getApplicationFormNo() {
        return applicationFormNo;
    }

    
    public void setApplicationFormNo(String applicationFormNo) {
        this.applicationFormNo = applicationFormNo;
    }

    
    public String getBillDate() {
        return billDate;
    }

    
    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    
    public String getEffectiveDate() {
        return effectiveDate;
    }

    
    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    
    public String getExpireDate() {
        return expireDate;
    }

    
    public void setExpireDate(String expireDate) {
        this.expireDate = expireDate;
    }

    
    public String getPeriodDesc() {
        return periodDesc;
    }

    
    public void setPeriodDesc(String periodDesc) {
        this.periodDesc = periodDesc;
    }

    
    public String getSpecialAgreementDesc() {
        return specialAgreementDesc;
    }

    
    public void setSpecialAgreementDesc(String specialAgreementDesc) {
        this.specialAgreementDesc = specialAgreementDesc;
    }

    
    public String getAgent() {
        return agent;
    }

    
    public void setAgent(String agent) {
        this.agent = agent;
    }

    
    public String getOperator() {
        return operator;
    }

    
    public void setOperator(String operator) {
        this.operator = operator;
    }

    
    public String getCurrencyCode() {
        return currencyCode;
    }

    
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    
    public String getTotalBasicPremium() {
        return totalBasicPremium;
    }

    
    public void setTotalBasicPremium(String totalBasicPremium) {
        this.totalBasicPremium = totalBasicPremium;
    }

    
    public String getTotalStandardPremium() {
        return totalStandardPremium;
    }

    
    public void setTotalStandardPremium(String totalStandardPremium) {
        this.totalStandardPremium = totalStandardPremium;
    }

    
    public String getTotalPremium() {
        return totalPremium;
    }

    
    public void setTotalPremium(String totalPremium) {
        this.totalPremium = totalPremium;
    }

    
    public String getAreaFlag() {
        return areaFlag;
    }

    
    public void setAreaFlag(String areaFlag) {
        this.areaFlag = areaFlag;
    }

    
    public String getChannelCode() {
        return channelCode;
    }

    
    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    
    public String getByBusinessAgent() {
        return byBusinessAgent;
    }

    
    public void setByBusinessAgent(String byBusinessAgent) {
        this.byBusinessAgent = byBusinessAgent;
    }

    
    public String getSubordinateCode() {
        return subordinateCode;
    }

    
    public void setSubordinateCode(String subordinateCode) {
        this.subordinateCode = subordinateCode;
    }

    
    public String getPolicyStatus() {
        return policyStatus;
    }

    
    public void setPolicyStatus(String policyStatus) {
        this.policyStatus = policyStatus;
    }

    
    public String getPayType() {
        return payType;
    }

    
    public void setPayType(String payType) {
        this.payType = payType;
    }

    
    public String getReinsrcFlg() {
        return reinsrcFlg;
    }

    
    public void setReinsrcFlg(String reinsrcFlg) {
        this.reinsrcFlg = reinsrcFlg;
    }

    
    public String getProductType() {
        return productType;
    }

    
    public void setProductType(String productType) {
        this.productType = productType;
    }

    
    public String getPlanCode() {
        return planCode;
    }

    
    public void setPlanCode(String planCode) {
        this.planCode = planCode;
    }

    
    public String getSttl() {
        return sttl;
    }

    
    public void setSttl(String sttl) {
        this.sttl = sttl;
    }

    
    public String getDrvRate() {
        return drvRate;
    }

    
    public void setDrvRate(String drvRate) {
        this.drvRate = drvRate;
    }

    
    public String getSpecialFlag() {
        return specialFlag;
    }

    
    public void setSpecialFlag(String specialFlag) {
        this.specialFlag = specialFlag;
    }

    
    public String getUnderwritingType() {
        return underwritingType;
    }

    
    public void setUnderwritingType(String underwritingType) {
        this.underwritingType = underwritingType;
    }

    
    public String getUnderwritingMemo() {
        return underwritingMemo;
    }

    
    public void setUnderwritingMemo(String underwritingMemo) {
        this.underwritingMemo = underwritingMemo;
    }

    
    public String getUnderwritingEmpno() {
        return underwritingEmpno;
    }

    
    public void setUnderwritingEmpno(String underwritingEmpno) {
        this.underwritingEmpno = underwritingEmpno;
    }

    
    public String getUnderwritingDate() {
        return underwritingDate;
    }

    
    public void setUnderwritingDate(String underwritingDate) {
        this.underwritingDate = underwritingDate;
    }

    
    public String getEdrPrjDate() {
        return edrPrjDate;
    }

    
    public void setEdrPrjDate(String edrPrjDate) {
        this.edrPrjDate = edrPrjDate;
    }

    
    public String getCancleDate() {
        return cancleDate;
    }

    
    public void setCancleDate(String cancleDate) {
        this.cancleDate = cancleDate;
    }

    
    public String getCancleReason() {
        return cancleReason;
    }

    
    public void setCancleReason(String cancleReason) {
        this.cancleReason = cancleReason;
    }

    
    public String getCancleReasonMemo() {
        return cancleReasonMemo;
    }

    
    public void setCancleReasonMemo(String cancleReasonMemo) {
        this.cancleReasonMemo = cancleReasonMemo;
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

    
    public List<Applicant> getApplicants() {
        return applicants;
    }

    
    public void setApplicants(List<Applicant> applicants) {
        this.applicants = applicants;
    }

    
    public List<Insured> getInsureds() {
        return insureds;
    }

    
    public void setInsureds(List<Insured> insureds) {
        this.insureds = insureds;
    }

    
    public VehicleOwner getVehicleOwner() {
        return vehicleOwner;
    }

    
    public void setVehicleOwner(VehicleOwner vehicleOwner) {
        this.vehicleOwner = vehicleOwner;
    }

    
    public List<CoverageItem> getCoverageItems() {
        return coverageItems;
    }

    
    public void setCoverageItems(List<CoverageItem> coverageItems) {
        this.coverageItems = coverageItems;
    }

    
    public List<Endorsement> getEndorsements() {
        return endorsements;
    }

    
    public void setEndorsements(List<Endorsement> endorsements) {
        this.endorsements = endorsements;
    }

    
    public PolicyAgent getPolicyAgent() {
        return policyAgent;
    }

    
    public void setPolicyAgent(PolicyAgent policyAgent) {
        this.policyAgent = policyAgent;
    }

    
    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    
    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    
    public List<SpecifiedDriver> getSpecifiedDrivers() {
        return specifiedDrivers;
    }

    
    public void setSpecifiedDrivers(List<SpecifiedDriver> specifiedDrivers) {
        this.specifiedDrivers = specifiedDrivers;
    }
    
    
}
