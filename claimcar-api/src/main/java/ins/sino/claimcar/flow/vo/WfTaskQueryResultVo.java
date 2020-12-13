package ins.sino.claimcar.flow.vo;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 工作流查询结果Vo
 * @author ★LiuPing
 * @CreateTime 2016年1月09日
 */
public class WfTaskQueryResultVo extends PrpLWfTaskVo {

	private static final long serialVersionUID = 1L;

	/*来自 PrpLWfTaskQuery的复制 start */
	private String registNo;
	private String policyNo;
	private String policyNoLink;
	private String mercyFlag;
	private String customerLevel;
	private String isOnSitReport;
	private String licenseNo;
	private String frameNo;//--车架号VIN
	private String insuredCode;
	private String insuredName;//被保险人
	private String insuredIdNo;
	private String reportorName;
	private String reportorPhone;
	private String policyType;// 保单号类型中文商业，交强，商业+交强
	private String comCodePly;// 承保机构

	private Date reportTime;
	private Date endCaseTime;
	private Date damageTime;//出险时间
	/*来自 PrpLWfTaskQuery的复制  end*/

	// 经过计算得到
	private String overTime;// 超时时间

	/*以下属性是为了datatables不报错添加的，*/
	// regist
	private String damageAddress;// 出险地点
	
	// 调度提交查勘和人伤跟踪时，需要将如下数据保存到xml里面，用于已调度未接收任务
	private String assignUserPhone;// 接收人电话
	private String schedFirstTime;// 第一次调度时间
	private String schedLastTime;// 最后一次调度时间


	//ckeck添加的
	private String scheduleType;//任务类型
	private String license;//具体任务车牌号
	private String modelName;//车型名称
	private BigDecimal lossFee;//查勘估损金额
	
	//车辆定损添加
	private String deflossCarType;//损失方
	private BigDecimal sumLossFee;//定损金额(待核价金额或者待核损金额)
	private String sumVeriLoss;//核损金额
	private String sumVeripLoss;//核价金额(待核损金额)
	private String sumVeriLossFee;//核损金额
	//财产核损添加
	private String underwriteName;//处理人
	private String underWriteFlag;//案件标志
	//人伤添加
	private Date appointmentTime;//预约时间
	private int reportDay;//已报案天数
	private int appointmentDay;//距预约天数
	private String reconcileFlag;//是否现场调解
	//车辆核损处理查询页面增加
	private String isRecheck ;//是否复勘提交
	//车辆核价添加
	private String serialNo;//序号
	private String riskCode;//险种代码
	private String taskInNodeName;

	//垫付添加
	private String claimNo;//立案号
	
	private String compensateNo;//计算书号
	
	//预付添加
	private String createUser;
	private Date underwriteDate;//核赔通过时间
	private Double sumPay;//总预付赔款
	private Double sumFee;//总预付费用
	
	private String applyReason;
	private Date claimCancelTime;
	private Date claimTime;
	private Date claimRecoverTime;
	private String recoverReason;
	private String isSubRogation;
	
	//追偿添加
	private String replevyType;//追偿类型
	private String sumPlanReplevy;//计划追偿金额
	private String sumDefloss;//财产定损
	
	private long appointTaskInTime;//距流入时间
	
	private String intermCode;//公估机构
	private String assignUser;//处理人
	private String checkCode;//查勘机构
	//金额
	private BigDecimal money;

	private BigDecimal money1;
	private BigDecimal money2;
	//是否退回案子
	private String backFlags;
	//理算金额
	private BigDecimal compensateAmount;
	private String isVLoss;//是否核损完成
	
	private String timeOut;
	
	private Date createTime;
	
	public BigDecimal getCompensateAmount() {
		return compensateAmount;
	}

	public void setCompensateAmount(BigDecimal compensateAmount) {
		this.compensateAmount = compensateAmount;
	}

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



	public String getPolicyNoLink() {
		return policyNoLink;
	}

	public void setPolicyNoLink(String policyNoLink) {
		this.policyNoLink = policyNoLink;
	}

	public String getMercyFlag() {
		return mercyFlag;
	}

	public void setMercyFlag(String mercyFlag) {
		this.mercyFlag = mercyFlag;
	}

	public String getCustomerLevel() {
		return customerLevel;
	}

	public void setCustomerLevel(String customerLevel) {
		this.customerLevel = customerLevel;
	}

	public String getIsOnSitReport() {
		return isOnSitReport;
	}

	public void setIsOnSitReport(String isOnSitReport) {
		this.isOnSitReport = isOnSitReport;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getFrameNo() {
		return frameNo;
	}

	public void setFrameNo(String frameNo) {
		this.frameNo = frameNo;
	}

	public String getInsuredCode() {
		return insuredCode;
	}

	public void setInsuredCode(String insuredCode) {
		this.insuredCode = insuredCode;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getInsuredIdNo() {
		return insuredIdNo;
	}

	public void setInsuredIdNo(String insuredIdNo) {
		this.insuredIdNo = insuredIdNo;
	}

	public String getReportorName() {
		return reportorName;
	}

	public void setReportorName(String reportorName) {
		this.reportorName = reportorName;
	}

	public String getReportorPhone() {
		return reportorPhone;
	}

	public void setReportorPhone(String reportorPhone) {
		this.reportorPhone = reportorPhone;
	}

	public Date getReportTime() {
		return reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
	}

	public Date getEndCaseTime() {
		return endCaseTime;
	}

	public void setEndCaseTime(Date endCaseTime) {
		this.endCaseTime = endCaseTime;
	}

	public Date getDamageTime() {
		return damageTime;
	}

	public void setDamageTime(Date damageTime) {
		this.damageTime = damageTime;
	}
	public BigDecimal getLossFee() {
		return lossFee;
	}

	public void setLossFee(BigDecimal lossFee) {
		this.lossFee = lossFee;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public BigDecimal getSumLossFee() {
		return sumLossFee;
	}

	public void setSumLossFee(BigDecimal sumLossFee) {
		this.sumLossFee = sumLossFee;
	}

	
	public Date getAppointmentTime() {
		return appointmentTime;
	}

	public void setAppointmentTime(Date appointmentTime) {
		this.appointmentTime = appointmentTime;
	}

	public String getReconcileFlag() {
		return reconcileFlag;
	}

	public void setReconcileFlag(String reconcileFlag) {
		this.reconcileFlag = reconcileFlag;
	}

	public int getReportDay() {
		return reportDay;
	}

	public void setReportDay(int reportDay) {
		this.reportDay = reportDay;
	}

	public String getIsRecheck() {
		return isRecheck;
	}

	public void setIsRecheck(String isRecheck) {
		this.isRecheck = isRecheck;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	
	public String getRiskCode() {
		return riskCode;
	}

	
	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	public int getAppointmentDay() {
		return appointmentDay;
	}

	
	public void setAppointmentDay(int appointmentDay) {
		this.appointmentDay = appointmentDay;
	}

	
	public String getLicense() {
		return license;
	}

	
	public void setLicense(String license) {
		this.license = license;
	}

	
	public String getSumVeriLoss() {
		return sumVeriLoss;
	}

	
	public void setSumVeriLoss(String sumVeriLoss) {
		this.sumVeriLoss = sumVeriLoss;
	}

	
	public String getSumVeripLoss() {
		return sumVeripLoss;
	}

	
	public void setSumVeripLoss(String sumVeripLoss) {
		this.sumVeripLoss = sumVeripLoss;
	}

	
	public String getComCodePly() {
		return comCodePly;
	}

	
	public void setComCodePly(String comCodePly) {
		this.comCodePly = comCodePly;
	}

	
	public String getScheduleType() {
		return scheduleType;
	}

	
	public void setScheduleType(String scheduleType) {
		this.scheduleType = scheduleType;
	}

	
	public String getDeflossCarType() {
		return deflossCarType;
	}

	
	public void setDeflossCarType(String deflossCarType) {
		this.deflossCarType = deflossCarType;
	}

	
	public String getUnderwriteName() {
		return underwriteName;
	}

	
	public void setUnderwriteName(String underwriteName) {
		this.underwriteName = underwriteName;
	}

	
	public String getUnderWriteFlag() {
		return underWriteFlag;
	}

	
	public void setUnderWriteFlag(String underWriteFlag) {
		this.underWriteFlag = underWriteFlag;
	}

	
	public String getPolicyType() {
		return policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}


	public String getDamageAddress() {
		return damageAddress;
	}

	public void setDamageAddress(String damageAddress) {
		this.damageAddress = damageAddress;
	}

	public String getAssignUserPhone() {
		return assignUserPhone;
	}

	public void setAssignUserPhone(String assignUserPhone) {
		this.assignUserPhone = assignUserPhone;
	}

	public String getSchedFirstTime() {
		return schedFirstTime;
	}

	public void setSchedFirstTime(String schedFirstTime) {
		this.schedFirstTime = schedFirstTime;
	}

	public String getSchedLastTime() {
		return schedLastTime;
	}

	public void setSchedLastTime(String schedLastTime) {
		this.schedLastTime = schedLastTime;
	}

	public String getOverTime() {
		return overTime;
	}

	public void setOverTime(String overTime) {
		this.overTime = overTime;
	}

	public String getTaskInNodeName() {
		return taskInNodeName;
	}

	public void setTaskInNodeName(String taskInNodeName) {
		this.taskInNodeName = taskInNodeName;
	}

	public String getClaimNo() {
		return claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}


	public String getApplyReason() {
		return applyReason;
	}

	public void setApplyReason(String applyReason) {
		this.applyReason = applyReason;
	}

	public String getCompensateNo() {
		return compensateNo;
	}

	public void setCompensateNo(String compensateNo) {
		this.compensateNo = compensateNo;
	}

	
	public Date getUnderwriteDate() {
		return underwriteDate;
	}

	
	public void setUnderwriteDate(Date underwriteDate) {
		this.underwriteDate = underwriteDate;
	}

	
	public Double getSumPay() {
		return sumPay;
	}

	
	public void setSumPay(Double sumPay) {
		this.sumPay = sumPay;
	}

	
	public Double getSumFee() {
		return sumFee;
	}

	
	public void setSumFee(Double sumFee) {
		this.sumFee = sumFee;
	}

	public Date getClaimCancelTime() {
		return claimCancelTime;
	}

	public void setClaimCancelTime(Date claimCancelTime) {
		this.claimCancelTime = claimCancelTime;
	}

	public Date getClaimTime() {
		return claimTime;
	}

	public void setClaimTime(Date claimTime) {
		this.claimTime = claimTime;
	}

	public String getRecoverReason() {
		return recoverReason;
	}

	public void setRecoverReason(String recoverReason) {
		this.recoverReason = recoverReason;
	}

	public Date getClaimRecoverTime() {
		return claimRecoverTime;
	}

	public void setClaimRecoverTime(Date claimRecoverTime) {
		this.claimRecoverTime = claimRecoverTime;
	}

	
	/**
	 * @return 返回 isSubRogation。
	 */
	public String getIsSubRogation() {
		return isSubRogation;
	}

	
	/**
	 * @param isSubRogation 要设置的 isSubRogation。
	 */
	public void setIsSubRogation(String isSubRogation) {
		this.isSubRogation = isSubRogation;
	}

	public String getReplevyType() {
		return replevyType;
	}

	public void setReplevyType(String replevyType) {
		this.replevyType = replevyType;
	}

	public String getSumPlanReplevy() {
		return sumPlanReplevy;
	}

	public void setSumPlanReplevy(String sumPlanReplevy) {
		this.sumPlanReplevy = sumPlanReplevy;
	}

	public String getSumDefloss() {
		return sumDefloss;
	}

	public void setSumDefloss(String sumDefloss) {
		this.sumDefloss = sumDefloss;
	}

	public String getSumVeriLossFee() {
		return sumVeriLossFee;
	}

	public void setSumVeriLossFee(String sumVeriLossFee) {
		this.sumVeriLossFee = sumVeriLossFee;
	}

	public long getAppointTaskInTime() {
		return appointTaskInTime;
	}

	public void setAppointTaskInTime(long appointTaskInTime) {
		this.appointTaskInTime = appointTaskInTime;
	}

	
	public String getIntermCode() {
		return intermCode;
	}

	
	public void setIntermCode(String intermCode) {
		this.intermCode = intermCode;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getMoney1() {
		return money1;
	}

	public void setMoney1(BigDecimal money1) {
		this.money1 = money1;
	}

	public BigDecimal getMoney2() {
		return money2;
	}

	public void setMoney2(BigDecimal money2) {
		this.money2 = money2;
	}

	public String getBackFlags() {
		return backFlags;
	}

	public void setBackFlags(String backFlags) {
		this.backFlags = backFlags;
	}

    
    public String getIsVLoss() {
        return isVLoss;
    }

    
    public void setIsVLoss(String isVLoss) {
        this.isVLoss = isVLoss;
    }

	public String getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(String timeOut) {
		this.timeOut = timeOut;
	}

	public String getAssignUser() {
		return assignUser;
	}

	public void setAssignUser(String assignUser) {
		this.assignUser = assignUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	
	

}
