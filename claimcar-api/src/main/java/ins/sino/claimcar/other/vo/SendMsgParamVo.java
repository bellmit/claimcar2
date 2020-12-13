package ins.sino.claimcar.other.vo;

import ins.sino.claimcar.regist.vo.PrpLCItemKindVo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SendMsgParamVo implements java.io.Serializable{
	
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 private String phoneNo="";//接收短信的手机号
	 
	 private String useCode="";//发送者编号
	 
	 private String comCode="";//发送者归属机构
	
	 private String registNo="";//报案号
	 
	 private String insuredName=""; //被保险人名称
	 
	 private String mobile="";//被保险人手机
	 
	 private String licenseNo="";//车牌号
	 
	 private String brandName="";//车辆类型
	 
	 private Date damageTime;//出险时间
	 
	 private String damageAddress="";//出险地点
	 
	 private String linkerName="";//联系人
	 
	 private String linkerMobile="";//联系人电话
	 
	 private String scheduledUsername="";//现场查勘员/定损人员
	 
	 private String scheduledMobile="";//查勘员电话
	 
     private String personScheduledUsername="";//人伤跟踪员
	 
	 private String personScheduledMobile="";//人伤跟踪员电话
	 
	 private String checkAddress="";//查勘地点
	 
	 private String kindCode="";//险别
	 
	 private String repairName="";//推荐修理厂（4S店名称）
	 
	 private String dLossAddress="";//定损地点
	 
	 private String losspart="";//损失部位（取报案的）
	 
	 private String repairAddress="";//修理厂地址
	 
	 private String repairMobile="";//修理厂联系电话
	 
	 private String repairLinker="";//修理厂联系人
	 
	 private String insuredDate=""; //保险期间（商业-交强）
	 
	 private String injuredcount="0";//受伤人数
	 
	 private String deathcount="0";//死亡人数
	 
	 private String damageReason="";//出险原因
	 
	 private Boolean isPersonLoss;//是否有人伤
	 
	 private String reportorName="";//报案人
	 
	 private String reportoMobile="";//报案人电话
	 
	 private String reportTime="";//报案时间
	 
	 private String policyNo="";//保单号
	 
	 private String frameNo="";//车架号
	 
	 private String dangerRemark="";//出险经过
	 
	 private String otherLicenseNo="";//三者车牌号
	 
	 private String scheduledTime="";//调度时间
	 
	 private String riskType="";//商业/交强

	 private String sumAmt="";//赔款金额

	 private String accountNo="";//银行账户

	 private String payeeName="";//收款人名称
	 
	 private String driverName="";//驾驶人名称
	 
	 private String driverPhone="";//驾驶人电话
	 
	 private String summary;//摘要
	 
	 private String thridInjuredcount="0";//三者车受伤人数
	 
	 private String thridDeathcount="0";//三者车死亡人数
	 
     private String ItemInjuredcount="0";//标的车受伤人数
	 
	 private String ItemDeathcount="0";//标的车死亡人数
	 
	 private String agentPhone="";//代理人手机号码
	 
	 private String registTimes_BI="";//商业险出险次数
	 
	 private String registTimes_CI="";//交强险出险次数

	private String  businessPlate = ""; //业务板块
	 
	 private String businessClassCheckMsg = ""; //会员分类
	 
	 private String modelType;	//模板类型
	 
	 private String systemNode;	//发送节点
	/** 是否核心客户 */
	private String isCoreCustomer;

	/**
	 * <p>业务员  code-name格式  数据来源：同保单信息查看中的业务员数据Prplcmain.handler1code-handler1name<br>
	 * 1639 关于车险理赔系统查勘员报案通知短信内容中增加业务员名字需求
	 * </p>
	 */
	private String handler;

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	private List<PrpLCItemKindVo> prpCItemKinds = new ArrayList<PrpLCItemKindVo>(0);//报案时保单险别表
	 
	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public Date getDamageTime() {
		return damageTime;
	}

	public void setDamageTime(Date damageTime) {
		this.damageTime = damageTime;
	}

	public String getDamageAddress() {
		return damageAddress;
	}

	public void setDamageAddress(String damageAddress) {
		this.damageAddress = damageAddress;
	}

	public String getLinkerName() {
		return linkerName;
	}

	public void setLinkerName(String linkerName) {
		this.linkerName = linkerName;
	}

	public String getScheduledUsername() {
		return scheduledUsername;
	}

	public void setScheduledUsername(String scheduledUsername) {
		this.scheduledUsername = scheduledUsername;
	}

	public String getScheduledMobile() {
		return scheduledMobile;
	}

	public void setScheduledMobile(String scheduledMobile) {
		this.scheduledMobile = scheduledMobile;
	}

	public String getCheckAddress() {
		return checkAddress;
	}

	public void setCheckAddress(String checkAddress) {
		this.checkAddress = checkAddress;
	}

	public String getKindCode() {
		return kindCode;
	}

	public void setKindCode(String kindCode) {
		this.kindCode = kindCode;
	}

	public String getRepairName() {
		return repairName;
	}

	public void setRepairName(String repairName) {
		this.repairName = repairName;
	}

	public String getdLossAddress() {
		return dLossAddress;
	}

	public void setdLossAddress(String dLossAddress) {
		this.dLossAddress = dLossAddress;
	}

	public String getLosspart() {
		return losspart;
	}

	public void setLosspart(String losspart) {
		this.losspart = losspart;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getLinkerMobile() {
		return linkerMobile;
	}

	public void setLinkerMobile(String linkerMobile) {
		this.linkerMobile = linkerMobile;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getUseCode() {
		return useCode;
	}

	public void setUseCode(String useCode) {
		this.useCode = useCode;
	}

	public String getComCode() {
		return comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public String getRepairAddress() {
		return repairAddress;
	}

	public void setRepairAddress(String repairAddress) {
		this.repairAddress = repairAddress;
	}

	public String getRepairMobile() {
		return repairMobile;
	}

	public void setRepairMobile(String repairMobile) {
		this.repairMobile = repairMobile;
	}

	public String getRepairLinker() {
		return repairLinker;
	}

	public void setRepairLinker(String repairLinker) {
		this.repairLinker = repairLinker;
	}

	public String getPersonScheduledUsername() {
		return personScheduledUsername;
	}

	public void setPersonScheduledUsername(String personScheduledUsername) {
		this.personScheduledUsername = personScheduledUsername;
	}

	public String getPersonScheduledMobile() {
		return personScheduledMobile;
	}

	public void setPersonScheduledMobile(String personScheduledMobile) {
		this.personScheduledMobile = personScheduledMobile;
	}

	public String getInsuredDate() {
		return insuredDate;
	}

	public void setInsuredDate(String insuredDate) {
		this.insuredDate = insuredDate;
	}

	public String getInjuredcount() {
		return injuredcount;
	}

	public void setInjuredcount(String injuredcount) {
		this.injuredcount = injuredcount;
	}

	public String getDeathcount() {
		return deathcount;
	}

	public void setDeathcount(String deathcount) {
		this.deathcount = deathcount;
	}

	public String getDamageReason() {
		return damageReason;
	}

	public void setDamageReason(String damageReason) {
		this.damageReason = damageReason;
	}

	public Boolean getIsPersonLoss() {
		return isPersonLoss;
	}

	public void setIsPersonLoss(Boolean isPersonLoss) {
		this.isPersonLoss = isPersonLoss;
	}

	public String getReportorName() {
		return reportorName;
	}

	public void setReportorName(String reportorName) {
		this.reportorName = reportorName;
	}

	public String getReportoMobile() {
		return reportoMobile;
	}

	public void setReportoMobile(String reportoMobile) {
		this.reportoMobile = reportoMobile;
	}

	public String getReportTime() {
		return reportTime;
	}

	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getFrameNo() {
		return frameNo;
	}

	public void setFrameNo(String frameNo) {
		this.frameNo = frameNo;
	}

	public String getDangerRemark() {
		return dangerRemark;
	}

	public void setDangerRemark(String dangerRemark) {
		this.dangerRemark = dangerRemark;
	}

	public String getOtherLicenseNo() {
		return otherLicenseNo;
	}

	public void setOtherLicenseNo(String otherLicenseNo) {
		this.otherLicenseNo = otherLicenseNo;
	}

	public String getScheduledTime() {
		return scheduledTime;
	}

	public void setScheduledTime(String scheduledTime) {
		this.scheduledTime = scheduledTime;
	}

	public String getRiskType() {
		return riskType;
	}

	public void setRiskType(String riskType) {
		this.riskType = riskType;
	}

	public String getSumAmt() {
		return sumAmt;
	}

	public void setSumAmt(String sumAmt) {
		this.sumAmt = sumAmt;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}

	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDriverPhone() {
		return driverPhone;
	}

	public void setDriverPhone(String driverPhone) {
		this.driverPhone = driverPhone;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getThridInjuredcount() {
		return thridInjuredcount;
	}

	public void setThridInjuredcount(String thridInjuredcount) {
		this.thridInjuredcount = thridInjuredcount;
	}

	public String getThridDeathcount() {
		return thridDeathcount;
	}

	public void setThridDeathcount(String thridDeathcount) {
		this.thridDeathcount = thridDeathcount;
	}

	public String getItemInjuredcount() {
		return ItemInjuredcount;
	}

	public void setItemInjuredcount(String itemInjuredcount) {
		ItemInjuredcount = itemInjuredcount;
	}

	public String getItemDeathcount() {
		return ItemDeathcount;
	}

	public void setItemDeathcount(String itemDeathcount) {
		ItemDeathcount = itemDeathcount;
	}

	public String getAgentPhone() {
		return agentPhone;
	}

	public void setAgentPhone(String agentPhone) {
		this.agentPhone = agentPhone;
	}

	public String getRegistTimes_BI() {
		return registTimes_BI;
	}

	public void setRegistTimes_BI(String registTimes_BI) {
		this.registTimes_BI = registTimes_BI;
	}

	public String getRegistTimes_CI() {
		return registTimes_CI;
	}

	public void setRegistTimes_CI(String registTimes_CI) {
		this.registTimes_CI = registTimes_CI;
	}

	 
	 public String getBusinessClassCheckMsg() {
		return businessClassCheckMsg;
	}

	public void setBusinessClassCheckMsg(String businessClassCheckMsg) {
		this.businessClassCheckMsg = businessClassCheckMsg;
	}

	public String getBusinessPlate() {
		return businessPlate;
	}

	public void setBusinessPlate(String businessPlate) {
		this.businessPlate = businessPlate;
	}
    
    public List<PrpLCItemKindVo> getPrpCItemKinds() {
        return prpCItemKinds;
    }
 
    public void setPrpCItemKinds(List<PrpLCItemKindVo> prpCItemKinds) {
        this.prpCItemKinds = prpCItemKinds;
    }

	public String getModelType() {
		return modelType;
	}

	public void setModelType(String modelType) {
		this.modelType = modelType;
	}

	public String getSystemNode() {
		return systemNode;
	}

	public void setSystemNode(String systemNode) {
		this.systemNode = systemNode;
	}

	public String getIsCoreCustomer() {
		return isCoreCustomer;
	}

	public void setIsCoreCustomer(String isCoreCustomer) {
		this.isCoreCustomer = isCoreCustomer;
	}
}
