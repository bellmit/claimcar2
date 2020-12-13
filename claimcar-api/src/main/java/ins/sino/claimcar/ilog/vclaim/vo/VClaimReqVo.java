package ins.sino.claimcar.ilog.vclaim.vo;


import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 车险核赔对外服务接口
 * <pre></pre>
 * @author ★zhujunde
 */
@XStreamAlias("root")
public class VClaimReqVo {

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("compensateNo")
	private String compensateNo = ""; //赔款计算书号码
	
    @XStreamAlias("registNo")
    private String registNo = ""; //报案号
    
    @XStreamAlias("isAuto")
    private String isAuto = ""; //人工自动标识
    
    @XStreamAlias("operateType")
    private String operateType = ""; //操作类型
    
    @XStreamAlias("curpowerLevel")
    private String curpowerLevel = ""; //当前核赔级别
    
    @XStreamAlias("callNode")
    private String callNode = ""; //调用环节
    
    @XStreamAlias("comcode")
    private String comCode = ""; //归属机构代码
    
    @XStreamAlias("riskCode")
    private String riskCode = ""; //险种代码
    
    @XStreamAlias("CarKindCode")
    private String carKindCode = ""; //承保车辆种类A
    
    
    @XStreamAlias("deductCondCode")
    private String deductCondCode = ""; //免赔条件
    
    @XStreamAlias("damageDate")
    private String damageDate = ""; //出险日期
    
    @XStreamAlias("damageHour")
    private String damageHour = ""; //出险小时
    
    @XStreamAlias("damageMinute")
    private String damageMinute = ""; //出险分钟
    
    @XStreamAlias("startDate")
    private String startDate = ""; //起保日期
    
    @XStreamAlias("startHour")
    private String startHour = ""; //起保小时
    
    @XStreamAlias("startMinute")
    private String startMinute = ""; //起保分钟
    
    @XStreamAlias("endDate")
    private String endDate = ""; //终保日期
    
    @XStreamAlias("endHour")
    private String endHour = ""; //终保小时
    
    @XStreamAlias("endMinute")
    private String endMinute = ""; //终保分钟
    
    @XStreamAlias("repairFactoryType")
    private String repairFactoryType = ""; //修理厂类型
    
    
    @XStreamAlias("casualtiesCaseFlag")
    private String casualtiesCaseFlag = ""; //是否涉及人伤
    
    @XStreamAlias("adjustAmount")
    private String adjustAmount = ""; //理算提交金额
    
    @XStreamAlias("rescueFeeAmount")
    private String rescueFeeAmount = ""; //施救费金额
    
    
    @XStreamAlias("isPrepaidType")
    private String isPrepaidType = "";; //是否预付类型
    
    @XStreamAlias("majorcaseFlag")
    private String majorcaseFlag = "";; //重大赔案上报
    
    @XStreamAlias("isTotalLoss")
    private String isTotalLoss = "";; //是否全损
    
    @XStreamAlias("isJQFraud")
    private String isJQFraud = "";; //是否交强拒赔
    
    @XStreamAlias("isSYFraud")
    private String isSYFraud = "";; //是否商业拒赔
    
    @XStreamAlias("lawsuitFlag")
    private String lawsuitFlag = "";; //是否诉讼
    
    @XStreamAlias("subrogationFlag")
    private String subrogationFlag = "";; //是否代位求偿案件
    
    @XStreamAlias("allowFlag")
    private String allowFlag = "";; //是否通融
    
    @XStreamAlias("recoveryFlag")
    private String recoveryFlag = "";; //是否发起追偿
    
    @XStreamAlias("isCheat")
    private String isCheat = "";; //是否欺诈
    
    @XStreamAlias("isFlagN")
    private String isFlagN = "";; //是否可疑交易
    
    @XStreamAlias("isAssessmentSurvey")
    private String isAssessmentSurvey = "";; //是否公估查勘
    
    @XStreamAlias("surveyFlag")
    private String surveyFlag = "";; //是否调查
    
    @XStreamAlias("payeeInfoNum")
    private Integer payeeInfoNum; //报案号下收款人信息条数
    
    @XStreamAlias("JQDamagTime")
    private String jqDamagTime = "";; //出险次数（交强）
    
    @XStreamAlias("SYDamagTime")
    private String syDamagTime = "";; //出险次数（商业）
    
    @XStreamAlias("sumPaidFee")
    private String sumPaidFee = "";; //直接理赔费用(本次赔付费用)
    
    @XStreamAlias("validityCarFeeMissionNum")
    private String validityCarFeeMissionNum = "";; //有效车辆定损任务数量
    
    @XStreamAlias("lossFeeType")
    private String lossFeeType = "";; //损失类型
    
    @XStreamAlias("sumAmount")
    private String sumAmount = "";; //总赔款金额
    
    @XStreamAlias("adjustmentAmount")
    private String adjustmentAmount = "";; //当前理算金额
    
    @XStreamAlias("isReopenClaim")
    private String isReopenClaim = "";; //是否重开案件
    
    @XStreamAlias("isSurveyCase")
    private String isSurveyCase = "";; //是否代查勘案件
    
    @XStreamAlias("itemKindList")
    private List<ItemKind> itemKindList; //标的子险信息列表
    
    @XStreamAlias("accidentResponsibilityList")
    private List<AccidentResponsibility> accidentResponsibilityList; //事故责任比例列表

    @XStreamAlias("sysAuthorizationFlag")
    private String sysAuthorizationFlag; //是否系统授权 
    
    @XStreamAlias("coinsFlag")
    private String coinsFlag; //是否联共保案件
    
    @XStreamAlias("Isinvolvedincarlaitp")
    private String isinvolvedincarlaitp; //是否涉及车物减损金额录入项目
    
    @XStreamAlias("paymentInfoList")
    private List<PaymentInfo> paymentInfoList; // 收款人信息
    
    @XStreamAlias("repairInfoList")
    private List<RepairInfo> repairInfoList; // 维修信息
    
    @XStreamAlias("rescueFeeOfVerifyLoss")
    private String rescueFeeOfVerifyLoss; // 核损环节施救费总金额
    
    /**
     * 	车物赔款总金额
     */
    @XStreamAlias("propertySumAmount")
    private String propertySumAmount; 
    
    /**
     * 	人伤赔款总金额
     */
    @XStreamAlias("personSumAmount")
    private String personSumAmount;
    
    @XStreamAlias("existHeadOffice")
	private String existHeadOffice;
	
	
	/*是否被总公司审核过*/
	public String getExistHeadOffice() {
		return existHeadOffice;
	}
	
	public void setExistHeadOffice(String existHeadOffice) {
		this.existHeadOffice = existHeadOffice;
	}
    
	public String getPropertySumAmount() {
		return propertySumAmount;
	}

	public void setPropertySumAmount(String propertySumAmount) {
		this.propertySumAmount = propertySumAmount;
	}

	public String getPersonSumAmount() {
		return personSumAmount;
	}

	public void setPersonSumAmount(String personSumAmount) {
		this.personSumAmount = personSumAmount;
	}

	public String getRescueFeeOfVerifyLoss() {
		return rescueFeeOfVerifyLoss;
	}

	public void setRescueFeeOfVerifyLoss(String rescueFeeOfVerifyLoss) {
		this.rescueFeeOfVerifyLoss = rescueFeeOfVerifyLoss;
	}

	public List<PaymentInfo> getPaymentInfoList() {
		return paymentInfoList;
	}


	public void setPaymentInfoList(List<PaymentInfo> paymentInfoList) {
		this.paymentInfoList = paymentInfoList;
	}


	public List<RepairInfo> getRepairInfoList() {
		return repairInfoList;
	}


	public void setRepairInfoList(List<RepairInfo> repairInfoList) {
		this.repairInfoList = repairInfoList;
	}


	public String getCoinsFlag() {
		return coinsFlag;
	}


	public void setCoinsFlag(String coinsFlag) {
		this.coinsFlag = coinsFlag;
	}


	public String getCompensateNo() {
        return compensateNo;
    }

    
    public void setCompensateNo(String compensateNo) {
        this.compensateNo = compensateNo;
    }

    
    public String getRegistNo() {
        return registNo;
    }

    
    public void setRegistNo(String registNo) {
        this.registNo = registNo;
    }

    
    public String getIsAuto() {
        return isAuto;
    }

    
    public void setIsAuto(String isAuto) {
        this.isAuto = isAuto;
    }

    
    public String getOperateType() {
        return operateType;
    }

    
    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    
    public String getCurpowerLevel() {
        return curpowerLevel;
    }

    
    public void setCurpowerLevel(String curpowerLevel) {
        this.curpowerLevel = curpowerLevel;
    }

    
    public String getCallNode() {
        return callNode;
    }

    
    public void setCallNode(String callNode) {
        this.callNode = callNode;
    }

    
    public String getComCode() {
        return comCode;
    }

    
    public void setComCode(String comCode) {
        this.comCode = comCode;
    }

    
    public String getRiskCode() {
        return riskCode;
    }

    
    public void setRiskCode(String riskCode) {
        this.riskCode = riskCode;
    }

    
    public String getCarKindCode() {
        return carKindCode;
    }

    
    public void setCarKindCode(String carKindCode) {
        this.carKindCode = carKindCode;
    }

    
    public String getDeductCondCode() {
        return deductCondCode;
    }

    
    public void setDeductCondCode(String deductCondCode) {
        this.deductCondCode = deductCondCode;
    }

    
    public String getDamageDate() {
        return damageDate;
    }

    
    public void setDamageDate(String damageDate) {
        this.damageDate = damageDate;
    }

    
    public String getDamageHour() {
        return damageHour;
    }

    
    public void setDamageHour(String damageHour) {
        this.damageHour = damageHour;
    }

    
    public String getDamageMinute() {
        return damageMinute;
    }

    
    public void setDamageMinute(String damageMinute) {
        this.damageMinute = damageMinute;
    }

    
    public String getStartDate() {
        return startDate;
    }

    
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    
    public String getStartHour() {
        return startHour;
    }

    
    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    
    public String getStartMinute() {
        return startMinute;
    }

    
    public void setStartMinute(String startMinute) {
        this.startMinute = startMinute;
    }

    
    public String getEndDate() {
        return endDate;
    }

    
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    
    public String getEndHour() {
        return endHour;
    }

    
    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    
    public String getEndMinute() {
        return endMinute;
    }

    
    public void setEndMinute(String endMinute) {
        this.endMinute = endMinute;
    }

    
    public String getRepairFactoryType() {
        return repairFactoryType;
    }

    
    public void setRepairFactoryType(String repairFactoryType) {
        this.repairFactoryType = repairFactoryType;
    }

    
    public String getCasualtiesCaseFlag() {
        return casualtiesCaseFlag;
    }

    
    public void setCasualtiesCaseFlag(String casualtiesCaseFlag) {
        this.casualtiesCaseFlag = casualtiesCaseFlag;
    }

    
    public String getAdjustAmount() {
        return adjustAmount;
    }

    
    public void setAdjustAmount(String adjustAmount) {
        this.adjustAmount = adjustAmount;
    }

    
    public String getRescueFeeAmount() {
        return rescueFeeAmount;
    }

    
    public void setRescueFeeAmount(String rescueFeeAmount) {
        this.rescueFeeAmount = rescueFeeAmount;
    }

    
    public String getIsPrepaidType() {
        return isPrepaidType;
    }

    
    public void setIsPrepaidType(String isPrepaidType) {
        this.isPrepaidType = isPrepaidType;
    }

    
    public String getMajorcaseFlag() {
        return majorcaseFlag;
    }

    
    public void setMajorcaseFlag(String majorcaseFlag) {
        this.majorcaseFlag = majorcaseFlag;
    }

    
    public String getIsTotalLoss() {
        return isTotalLoss;
    }

    
    public void setIsTotalLoss(String isTotalLoss) {
        this.isTotalLoss = isTotalLoss;
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

    
    public String getAllowFlag() {
        return allowFlag;
    }

    
    public void setAllowFlag(String allowFlag) {
        this.allowFlag = allowFlag;
    }

    
    public String getRecoveryFlag() {
        return recoveryFlag;
    }

    
    public void setRecoveryFlag(String recoveryFlag) {
        this.recoveryFlag = recoveryFlag;
    }

    
    public String getIsCheat() {
        return isCheat;
    }

    
    public void setIsCheat(String isCheat) {
        this.isCheat = isCheat;
    }

    
    public String getIsFlagN() {
        return isFlagN;
    }

    
    public void setIsFlagN(String isFlagN) {
        this.isFlagN = isFlagN;
    }

    
    public String getIsAssessmentSurvey() {
        return isAssessmentSurvey;
    }

    
    public void setIsAssessmentSurvey(String isAssessmentSurvey) {
        this.isAssessmentSurvey = isAssessmentSurvey;
    }

    
    public String getSurveyFlag() {
        return surveyFlag;
    }

    
    public void setSurveyFlag(String surveyFlag) {
        this.surveyFlag = surveyFlag;
    }

    
    public Integer getPayeeInfoNum() {
        return payeeInfoNum;
    }

    
    public void setPayeeInfoNum(Integer payeeInfoNum) {
        this.payeeInfoNum = payeeInfoNum;
    }

    
    public String getJqDamagTime() {
        return jqDamagTime;
    }

    
    public void setJqDamagTime(String jqDamagTime) {
        this.jqDamagTime = jqDamagTime;
    }

    
    public String getSyDamagTime() {
        return syDamagTime;
    }

    
    public void setSyDamagTime(String syDamagTime) {
        this.syDamagTime = syDamagTime;
    }

    
    public String getSumPaidFee() {
        return sumPaidFee;
    }

    
    public void setSumPaidFee(String sumPaidFee) {
        this.sumPaidFee = sumPaidFee;
    }

    
    public String getValidityCarFeeMissionNum() {
        return validityCarFeeMissionNum;
    }

    
    public void setValidityCarFeeMissionNum(String validityCarFeeMissionNum) {
        this.validityCarFeeMissionNum = validityCarFeeMissionNum;
    }

    
    public String getLossFeeType() {
        return lossFeeType;
    }

    
    public void setLossFeeType(String lossFeeType) {
        this.lossFeeType = lossFeeType;
    }

    
    public String getSumAmount() {
        return sumAmount;
    }

    
    public void setSumAmount(String sumAmount) {
        this.sumAmount = sumAmount;
    }

    
    public String getAdjustmentAmount() {
        return adjustmentAmount;
    }

    
    public void setAdjustmentAmount(String adjustmentAmount) {
        this.adjustmentAmount = adjustmentAmount;
    }

    
    public String getIsReopenClaim() {
        return isReopenClaim;
    }

    
    public void setIsReopenClaim(String isReopenClaim) {
        this.isReopenClaim = isReopenClaim;
    }

    
    public String getIsSurveyCase() {
        return isSurveyCase;
    }

    
    public void setIsSurveyCase(String isSurveyCase) {
        this.isSurveyCase = isSurveyCase;
    }

    
    public List<ItemKind> getItemKindList() {
        return itemKindList;
    }

    
    public void setItemKindList(List<ItemKind> itemKindList) {
        this.itemKindList = itemKindList;
    }

    
    public List<AccidentResponsibility> getAccidentResponsibilityList() {
        return accidentResponsibilityList;
    }

    
    public void setAccidentResponsibilityList(List<AccidentResponsibility> accidentResponsibilityList) {
        this.accidentResponsibilityList = accidentResponsibilityList;
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

    
}
