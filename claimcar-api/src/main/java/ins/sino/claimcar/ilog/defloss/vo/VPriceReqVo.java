package ins.sino.claimcar.ilog.defloss.vo;


import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import ins.sino.claimcar.ilog.vclaim.vo.SingleCarLossInfo;

@XStreamAlias("root")
public class VPriceReqVo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("registNo")
	private String registNo = ""; //报案号
	
    @XStreamAlias("currentNodeNo")
    private String currentNodeNo = ""; //当前审核级别
    
    @XStreamAlias("operateType")
    private String operateType = ""; //操作类型
    
    @XStreamAlias("riskCode")
    private String riskCode = ""; //险种代码
    
    @XStreamAlias("comCode")
    private String comCode = ""; //机构
    
    @XStreamAlias("taskType")
    private String taskType = ""; //任务类型
    
    @XStreamAlias("repairFactoryType")
    private String repairFactoryType = ""; //修理厂类型
    
    @XStreamAlias("damageDate")
    private String damageDate = ""; //出险日期
    
    
    @XStreamAlias("damageHour")
    private String damageHour = ""; //出险小时
    
    @XStreamAlias("damageMinute")
    private String damageMinute = ""; //出险分钟
    
    @XStreamAlias("enrollDate")
    private String enrollDate = ""; //初登日期
    
    @XStreamAlias("jyStandardFittingsNum")
    private String jyStandardFittingsNum = ""; //精友系统标准点选配件个数
    
    @XStreamAlias("jystandardSingleFittingsHighAmount")
    private String jystandardSingleFittingsHighAmount = ""; //精友系统标准点选单个配件最高金额
    
    @XStreamAlias("jyNonstandardFittingsNum")
    private String jyNonstandardFittingsNum = ""; //精友系统非标准点选配件个数
    
    @XStreamAlias("jyNonstandardSingleFittingsHighAmount")
    private String jyNonstandardSingleFittingsHighAmount = ""; //精友系统非标准点选单个配件最高金额
    
    @XStreamAlias("jystandardFittingsSumAmount")
    private String jystandardFittingsSumAmount = ""; //精友系统标准点选配件更换总金额
    
    
    
    @XStreamAlias("jyNonstandardFittingsSumAmount")
    private String jyNonstandardFittingsSumAmount = ""; //精友系统非标准点选配件更换总金额
    
    
    @XStreamAlias("jyFittingsSumAmount")
    private String jyFittingsSumAmount = ""; //精友系统点选配件更换总金额
    
    @XStreamAlias("subrogationFlag")
    private String subrogationFlag = ""; //是否代位求偿
    
    @XStreamAlias("coverageFlag")
    private String coverageFlag = ""; //是否承保机动车损失保险  
    
    @XStreamAlias("markCarAmount")
    private String markCarAmount = ""; //标的车定损金额
    
    @XStreamAlias("carAmount")
    private String carAmount = ""; //车损险保额 
    
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
    
    @XStreamAlias("chgOwnerFlag")
    private String chgOwnerFlag = ""; //标的车是否过户车 
    
    @XStreamAlias("reportDate")
    private String reportDate = ""; //报案日期
    
    @XStreamAlias("reportHour")
    private String reportHour = ""; //报案小时
    
    @XStreamAlias("reportMinute")
    private String reportMinute = ""; //报案分钟
    
    @XStreamAlias("JQDamagTime")
    private String jqDamagTime = ""; //出险次数
    
    @XStreamAlias("JQSevenDaysDamagTime")
    private String jqSevenDaysDamagTime = ""; //7天内出险次数
    
    @XStreamAlias("SYDamagTime")
    private String syDamagtime = ""; //出险次数（商业）
    
    @XStreamAlias("SYSevenDaysDamagTime")
    private String sySevenDaysDamagTime = ""; //7天内出险次数（商业）
    
    @XStreamAlias("fittingsChangeSumNum")
    private String fittingsChangeSumNum = ""; //零部件更换次数合计
    
    @XStreamAlias("UseKindCode")
    private String UseKindCode = ""; //标的车使用性质 
    
    @XStreamAlias("thirdVehicleCertainAmount")
    private String thirdVehicleCertainAmount = ""; //三者车财定损金额之和
    
    @XStreamAlias("certainAmount")
    private String certainAmount = ""; //定损金额（不含施救费）
    
    @XStreamAlias("certainSumAmount")
    private String certainSumAmount = ""; //定损金额合计
    
    @XStreamAlias("modificaCertainAmount")
    private String modificaCertainAmount = ""; //修改后定损金额
    
    @XStreamAlias("rescueFeeAmount")
    private String rescueFeeAmount = ""; //施救费金额
    
    @XStreamAlias("deductCondCode")
    private String deductCondCode = ""; //免赔条件
    
    @XStreamAlias("damageCode")
    private String damageCode = ""; //出险原因
    
    @XStreamAlias("sysAuthorizationFlag")
    private String sysAuthorizationFlag; //是否系统授权    
    
    @XStreamAlias("employeeId")
    private String employeeId = ""; //员工工号
    
    @XStreamAlias("lossPartyName")
    private String lossPartyName = "";//损失方
   
    @XStreamAlias("sumAmount")
    private String sumAmount = ""; //总定损金额
       
    @XStreamAlias("surveyFlag")
    private String surveyFlag = "0"; //是否调查案件
    
    @XStreamAlias("isFlagN")
    private String isFlagN = "0";  //是否可疑交易

    @XStreamAlias("isWhethertheloss")
    private String isWhethertheloss;  //是否可疑交易
       
    @XStreamAlias("isNuclearpricereturn")
    private String isNuclearpricereturn;  //是否核价退回案件
    
    @XStreamAlias("isNucleardamagereturn")
    private String isNucleardamagereturn;  //是否核损退回案件
    
    @XStreamAlias("jyingredientsSumAmount")
    private String jyingredientsSumAmount;  //精友系统辅料总金额

    @XStreamAlias("coinsFlag")
    private String coinsFlag;  //是否联共保案件
    
    @XStreamAlias("accessoriesList")
    private List<Accessories> accessoriesList;  //配件信息
    
    @XStreamAlias("existHeadOffice")
	private String  existHeadOffice;
	
	
	
	public String getExistHeadOffice() {
		return existHeadOffice;
	}
	
	public void setExistHeadOffice(String existHeadOffice) {
		this.existHeadOffice = existHeadOffice;
	}
    
    /**
     * 	单车损失信息
     */
    @XStreamAlias("singleCarLossInfoList")
    private List<SingleCarLossInfo> singleCarlossCarInfoList;
    
    /**
     *	处理人类型（1-司内人员 0-公估人员）
     */
    @XStreamAlias("handlerType")
    private String handlerType;
    
	public String getHandlerType() {
		return handlerType;
	}

	public void setHandlerType(String handlerType) {
		this.handlerType = handlerType;
	}

	public List<SingleCarLossInfo> getSingleCarlossCarInfoList() {
		return singleCarlossCarInfoList;
	}

	public void setSingleCarlossCarInfoList(List<SingleCarLossInfo> singleCarlossCarInfoList) {
		this.singleCarlossCarInfoList = singleCarlossCarInfoList;
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


    
    public String getCurrentNodeNo() {
        return currentNodeNo;
    }


    
    public void setCurrentNodeNo(String currentNodeNo) {
        this.currentNodeNo = currentNodeNo;
    }


    
    public String getOperateType() {
        return operateType;
    }


    
    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }


    
    public String getRiskCode() {
        return riskCode;
    }


    
    public void setRiskCode(String riskCode) {
        this.riskCode = riskCode;
    }


    
    public String getComCode() {
        return comCode;
    }


    
    public void setComCode(String comCode) {
        this.comCode = comCode;
    }


    
    public String getTaskType() {
        return taskType;
    }


    
    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }


    
    public String getRepairFactoryType() {
        return repairFactoryType;
    }


    
    public void setRepairFactoryType(String repairFactoryType) {
        this.repairFactoryType = repairFactoryType;
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


    
    public String getEnrollDate() {
        return enrollDate;
    }


    
    public void setEnrollDate(String enrollDate) {
        this.enrollDate = enrollDate;
    }


    
    public String getJyStandardFittingsNum() {
        return jyStandardFittingsNum;
    }


    
    public void setJyStandardFittingsNum(String jyStandardFittingsNum) {
        this.jyStandardFittingsNum = jyStandardFittingsNum;
    }


    
    public String getJystandardSingleFittingsHighAmount() {
        return jystandardSingleFittingsHighAmount;
    }


    
    public void setJystandardSingleFittingsHighAmount(String jystandardSingleFittingsHighAmount) {
        this.jystandardSingleFittingsHighAmount = jystandardSingleFittingsHighAmount;
    }


    
    public String getJyNonstandardFittingsNum() {
        return jyNonstandardFittingsNum;
    }


    
    public void setJyNonstandardFittingsNum(String jyNonstandardFittingsNum) {
        this.jyNonstandardFittingsNum = jyNonstandardFittingsNum;
    }


    
    public String getJyNonstandardSingleFittingsHighAmount() {
        return jyNonstandardSingleFittingsHighAmount;
    }


    
    public void setJyNonstandardSingleFittingsHighAmount(String jyNonstandardSingleFittingsHighAmount) {
        this.jyNonstandardSingleFittingsHighAmount = jyNonstandardSingleFittingsHighAmount;
    }


    
    public String getJystandardFittingsSumAmount() {
        return jystandardFittingsSumAmount;
    }


    
    public void setJystandardFittingsSumAmount(String jystandardFittingsSumAmount) {
        this.jystandardFittingsSumAmount = jystandardFittingsSumAmount;
    }


    
    public String getJyNonstandardFittingsSumAmount() {
        return jyNonstandardFittingsSumAmount;
    }


    
    public void setJyNonstandardFittingsSumAmount(String jyNonstandardFittingsSumAmount) {
        this.jyNonstandardFittingsSumAmount = jyNonstandardFittingsSumAmount;
    }


    
    public String getJyFittingsSumAmount() {
        return jyFittingsSumAmount;
    }


    
    public void setJyFittingsSumAmount(String jyFittingsSumAmount) {
        this.jyFittingsSumAmount = jyFittingsSumAmount;
    }


    
    public String getSubrogationFlag() {
        return subrogationFlag;
    }


    
    public void setSubrogationFlag(String subrogationFlag) {
        this.subrogationFlag = subrogationFlag;
    }


    
    public String getCoverageFlag() {
        return coverageFlag;
    }


    
    public void setCoverageFlag(String coverageFlag) {
        this.coverageFlag = coverageFlag;
    }


    
    public String getMarkCarAmount() {
        return markCarAmount;
    }


    
    public void setMarkCarAmount(String markCarAmount) {
        this.markCarAmount = markCarAmount;
    }


    
    public String getCarAmount() {
        return carAmount;
    }


    
    public void setCarAmount(String carAmount) {
        this.carAmount = carAmount;
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


    
    public String getChgOwnerFlag() {
        return chgOwnerFlag;
    }


    
    public void setChgOwnerFlag(String chgOwnerFlag) {
        this.chgOwnerFlag = chgOwnerFlag;
    }


    
    public String getReportDate() {
        return reportDate;
    }


    
    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }


    
    public String getReportHour() {
        return reportHour;
    }


    
    public void setReportHour(String reportHour) {
        this.reportHour = reportHour;
    }


    
    public String getReportMinute() {
        return reportMinute;
    }


    
    public void setReportMinute(String reportMinute) {
        this.reportMinute = reportMinute;
    }


    
    public String getJqDamagTime() {
        return jqDamagTime;
    }


    
    public void setJqDamagTime(String jqDamagTime) {
        this.jqDamagTime = jqDamagTime;
    }


    
    public String getJqSevenDaysDamagTime() {
        return jqSevenDaysDamagTime;
    }


    
    public void setJqSevenDaysDamagTime(String jqSevenDaysDamagTime) {
        this.jqSevenDaysDamagTime = jqSevenDaysDamagTime;
    }


    
    public String getSyDamagtime() {
        return syDamagtime;
    }


    
    public void setSyDamagtime(String syDamagtime) {
        this.syDamagtime = syDamagtime;
    }


    
    public String getSySevenDaysDamagTime() {
        return sySevenDaysDamagTime;
    }


    
    public void setSySevenDaysDamagTime(String sySevenDaysDamagTime) {
        this.sySevenDaysDamagTime = sySevenDaysDamagTime;
    }


    
    public String getFittingsChangeSumNum() {
        return fittingsChangeSumNum;
    }


    
    public void setFittingsChangeSumNum(String fittingsChangeSumNum) {
        this.fittingsChangeSumNum = fittingsChangeSumNum;
    }


    
    public String getUseKindCode() {
        return UseKindCode;
    }


    
    public void setUseKindCode(String useKindCode) {
        UseKindCode = useKindCode;
    }


    
    public String getThirdVehicleCertainAmount() {
        return thirdVehicleCertainAmount;
    }


    
    public void setThirdVehicleCertainAmount(String thirdVehicleCertainAmount) {
        this.thirdVehicleCertainAmount = thirdVehicleCertainAmount;
    }


    
    public String getCertainAmount() {
        return certainAmount;
    }


    
    public void setCertainAmount(String certainAmount) {
        this.certainAmount = certainAmount;
    }


    
    public String getCertainSumAmount() {
        return certainSumAmount;
    }


    
    public void setCertainSumAmount(String certainSumAmount) {
        this.certainSumAmount = certainSumAmount;
    }


    
    public String getModificaCertainAmount() {
        return modificaCertainAmount;
    }


    
    public void setModificaCertainAmount(String modificaCertainAmount) {
        this.modificaCertainAmount = modificaCertainAmount;
    }


    
    public String getRescueFeeAmount() {
        return rescueFeeAmount;
    }


    
    public void setRescueFeeAmount(String rescueFeeAmount) {
        this.rescueFeeAmount = rescueFeeAmount;
    }


    
    public String getDeductCondCode() {
        return deductCondCode;
    }


    
    public void setDeductCondCode(String deductCondCode) {
        this.deductCondCode = deductCondCode;
    }


    
    public String getDamageCode() {
        return damageCode;
    }


    
    public void setDamageCode(String damageCode) {
        this.damageCode = damageCode;
    }

    
    
    public String getSysAuthorizationFlag() {
        return sysAuthorizationFlag;
    }


    
    public void setSysAuthorizationFlag(String sysAuthorizationFlag) {
        this.sysAuthorizationFlag = sysAuthorizationFlag;
    }



    public String getEmployeeId() {
        return employeeId;
    }


    
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }


    
    public String getLossPartyName() {
        return lossPartyName;
    }


    
    public void setLossPartyName(String lossPartyName) {
        this.lossPartyName = lossPartyName;
    }


    
    public String getSumAmount() {
        return sumAmount;
    }


    
    public void setSumAmount(String sumAmount) {
        this.sumAmount = sumAmount;
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

    
    public String getIsWhethertheloss() {
        return isWhethertheloss;
    }

    
    public void setIsWhethertheloss(String isWhethertheloss) {
        this.isWhethertheloss = isWhethertheloss;
    }
    
    
    public String getIsNuclearpricereturn() {
        return isNuclearpricereturn;
    }

    
    public void setIsNuclearpricereturn(String isNuclearpricereturn) {
        this.isNuclearpricereturn = isNuclearpricereturn;
    }



    public String getIsNucleardamagereturn() {
        return isNucleardamagereturn;
    }

  
    public void setIsNucleardamagereturn(String isNucleardamagereturn) {
        this.isNucleardamagereturn = isNucleardamagereturn;
    }



    
    public String getJyingredientsSumAmount() {
        return jyingredientsSumAmount;
    }

    
    public void setJyingredientsSumAmount(String jyingredientsSumAmount) {
        this.jyingredientsSumAmount = jyingredientsSumAmount;
    }

   
    public List<Accessories> getAccessoriesList() {
        return accessoriesList;
    }

    
    public void setAccessoriesList(List<Accessories> accessoriesList) {
        this.accessoriesList = accessoriesList;
    }

}
