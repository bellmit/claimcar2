package ins.sino.claimcar.regist.vo;

// Generated by Hibernate Tools 3.2.5 (sinosoft version), Don't modify!
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Vo Base Class of PO PrpLCMain
 */ 
public class PrpLCMainVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private String registNo;
	private String claimNo;
	private String policyNo;
	private String groupID;
	private String groupName;
	private String classCode;
	private String riskCode;
	private String proposalNo;
	private String contractNo;
	private String policySort;
	private String printNo;
	private String businessNature;
	private String language;
	private String policyType;
	private String appliCode;
	private String appliName;
	private String appliAddress;
	private String insuredCode;
	private String insuredName;
	private String insuredAddress;
	private Date operateDate;
	private Date startDate;
	private Long startHour;
	private Date endDate;
	private Long endHour;
	private BigDecimal pureRate;
	private BigDecimal disRate;
	private BigDecimal discount;
	private String currency;
	private BigDecimal sumValue;
	private BigDecimal sumAmount;
	private BigDecimal sumDiscount;
	private BigDecimal sumPremium;
	private BigDecimal sumSubprem;
	private BigDecimal sumQuantity;
	private String judicalScope;
	private String autoTransRenewFlag;
	private String argueSolution;
	private String arbitBoardName;
	private BigDecimal payTimes;
	private BigDecimal endorseTimes;
	private BigDecimal registTimes;
	private BigDecimal claimTimes;
	private BigDecimal sumClaim;
	private String makeCom;
	private String operateSite;
	private String comCode;
	private String handlerCode;
	private String handler1Code;
	private String approverCode;
	private String underWriteCode;
	private String underWriteName;
	private String operatorCode;
	private Date inputDate;
	private BigDecimal inputHour;
	private Date underWriteEndDate;
	private Date statisticsYm;
	private String agentCode;
	private String coinsFlag;
	private String reinsFlag;
	private String allinsFlag;
	private String underWriteFlag;
	private String othFlag;
	private String validFlag;
	private String judicalCode;
	private BigDecimal disrate1;
	private String businessFlag;
	private String updaterCode;
	private Date updateDate;
	private String updateHour;
	private Date signDate;
	private String agreementNo;
	private String inquiryNo;
	private String payMode;
	private String shareHolderFlag;
	private String visaCode;
	private String manualType;
	private String nationFlag;
	private String jfeeFlag;
	private Date precheckDate;
	private String handlerName;
	private String handler1Name;
	private String payrefCode;
	private String payrefName;
	private Date payrefTime;
	private Date printTime;
	private String agriNature;
	private String serviceProviders;
	private String serviceType;
	private String services;
	private String billingWay;
	private String businessChannel;
	private String businessAgentType;
	private String uniteAgentChannel;
	private String currency1;
	private String currency2;
	private String twoAvoid;
	private String busiNatureNo;
	private String renewal;
	private String businessPlate;
	private String stockHolderCode;
	private String stockHolderName;
	private String farmFlag;
	private String otherBusinessFlag;
	private String businessClass;
	private BigDecimal peccancyCoeff;
	private BigDecimal claimCoeff;
	private String rateFloatFlag;
	private String agentName;
	private String flag;
	private String remark;
	private String createUser;
	private Date createTime;
	private String updateUser;
	private Date updateTime;
	private String validNo;
	private String endorseNo;
	/** 是否核心客户 */
	private String isCoreCustomer;
	private String isTwentyFlag; //2020条款交强险标志
	private List<PrpLCInsuredVo> prpCInsureds = new ArrayList<PrpLCInsuredVo>(0);
	private List<PrpLCItemCarVo> prpCItemCars = new ArrayList<PrpLCItemCarVo>(0);
	private List<PrpLCengageVo> prpCengages = new ArrayList<PrpLCengageVo>(0);
	private List<PrpLCItemKindVo> prpCItemKinds = new ArrayList<PrpLCItemKindVo>(0);
	private List<PrpLcCarDeviceVo> prpLcCarDevices = new ArrayList<PrpLcCarDeviceVo>(0);
	
    protected PrpLCMainVoBase() {
	
    }

    
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRegistNo() {
		return this.registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getClaimNo() {
		return this.claimNo;
	}

	public void setClaimNo(String claimNo) {
		this.claimNo = claimNo;
	}

	public String getPolicyNo() {
		return this.policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getGroupID() {
		return this.groupID;
	}

	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getClassCode() {
		return this.classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	public String getRiskCode() {
		return this.riskCode;
	}

	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	public String getProposalNo() {
		return this.proposalNo;
	}

	public void setProposalNo(String proposalNo) {
		this.proposalNo = proposalNo;
	}

	public String getContractNo() {
		return this.contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	public String getPolicySort() {
		return this.policySort;
	}

	public void setPolicySort(String policySort) {
		this.policySort = policySort;
	}

	public String getPrintNo() {
		return this.printNo;
	}

	public void setPrintNo(String printNo) {
		this.printNo = printNo;
	}

	public String getBusinessNature() {
		return this.businessNature;
	}

	public void setBusinessNature(String businessNature) {
		this.businessNature = businessNature;
	}

	public String getLanguage() {
		return this.language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getPolicyType() {
		return this.policyType;
	}

	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}

	public String getAppliCode() {
		return this.appliCode;
	}

	public void setAppliCode(String appliCode) {
		this.appliCode = appliCode;
	}

	public String getAppliName() {
		return this.appliName;
	}

	public void setAppliName(String appliName) {
		this.appliName = appliName;
	}

	public String getAppliAddress() {
		return this.appliAddress;
	}

	public void setAppliAddress(String appliAddress) {
		this.appliAddress = appliAddress;
	}

	public String getInsuredCode() {
		return this.insuredCode;
	}

	public void setInsuredCode(String insuredCode) {
		this.insuredCode = insuredCode;
	}

	public String getInsuredName() {
		return this.insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getInsuredAddress() {
		return this.insuredAddress;
	}

	public void setInsuredAddress(String insuredAddress) {
		this.insuredAddress = insuredAddress;
	}

	public Date getOperateDate() {
		return this.operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Long getStartHour() {
		return this.startHour;
	}

	public void setStartHour(Long startHour) {
		this.startHour = startHour;
	}

	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Long getEndHour() {
		return this.endHour;
	}

	public void setEndHour(Long endHour) {
		this.endHour = endHour;
	}

	public BigDecimal getPureRate() {
		return this.pureRate;
	}

	public void setPureRate(BigDecimal pureRate) {
		this.pureRate = pureRate;
	}

	public BigDecimal getDisRate() {
		return this.disRate;
	}

	public void setDisRate(BigDecimal disRate) {
		this.disRate = disRate;
	}

	public BigDecimal getDiscount() {
		return this.discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public String getCurrency() {
		return this.currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public BigDecimal getSumValue() {
		return this.sumValue;
	}

	public void setSumValue(BigDecimal sumValue) {
		this.sumValue = sumValue;
	}

	public BigDecimal getSumAmount() {
		return this.sumAmount;
	}

	public void setSumAmount(BigDecimal sumAmount) {
		this.sumAmount = sumAmount;
	}

	public BigDecimal getSumDiscount() {
		return this.sumDiscount;
	}

	public void setSumDiscount(BigDecimal sumDiscount) {
		this.sumDiscount = sumDiscount;
	}

	public BigDecimal getSumPremium() {
		return this.sumPremium;
	}

	public void setSumPremium(BigDecimal sumPremium) {
		this.sumPremium = sumPremium;
	}

	public BigDecimal getSumSubprem() {
		return this.sumSubprem;
	}

	public void setSumSubprem(BigDecimal sumSubprem) {
		this.sumSubprem = sumSubprem;
	}

	public BigDecimal getSumQuantity() {
		return this.sumQuantity;
	}

	public void setSumQuantity(BigDecimal sumQuantity) {
		this.sumQuantity = sumQuantity;
	}

	public String getJudicalScope() {
		return this.judicalScope;
	}

	public void setJudicalScope(String judicalScope) {
		this.judicalScope = judicalScope;
	}

	public String getAutoTransRenewFlag() {
		return this.autoTransRenewFlag;
	}

	public void setAutoTransRenewFlag(String autoTransRenewFlag) {
		this.autoTransRenewFlag = autoTransRenewFlag;
	}

	public String getArgueSolution() {
		return this.argueSolution;
	}

	public void setArgueSolution(String argueSolution) {
		this.argueSolution = argueSolution;
	}

	public String getArbitBoardName() {
		return this.arbitBoardName;
	}

	public void setArbitBoardName(String arbitBoardName) {
		this.arbitBoardName = arbitBoardName;
	}

	public BigDecimal getPayTimes() {
		return this.payTimes;
	}

	public void setPayTimes(BigDecimal payTimes) {
		this.payTimes = payTimes;
	}

	public BigDecimal getEndorseTimes() {
		return this.endorseTimes;
	}

	public void setEndorseTimes(BigDecimal endorseTimes) {
		this.endorseTimes = endorseTimes;
	}

	public BigDecimal getRegistTimes() {
		return this.registTimes;
	}

	public void setRegistTimes(BigDecimal registTimes) {
		this.registTimes = registTimes;
	}

	public BigDecimal getClaimTimes() {
		return this.claimTimes;
	}

	public void setClaimTimes(BigDecimal claimTimes) {
		this.claimTimes = claimTimes;
	}

	public BigDecimal getSumClaim() {
		return this.sumClaim;
	}

	public void setSumClaim(BigDecimal sumClaim) {
		this.sumClaim = sumClaim;
	}

	public String getMakeCom() {
		return this.makeCom;
	}

	public void setMakeCom(String makeCom) {
		this.makeCom = makeCom;
	}

	public String getOperateSite() {
		return this.operateSite;
	}

	public void setOperateSite(String operateSite) {
		this.operateSite = operateSite;
	}

	public String getComCode() {
		return this.comCode;
	}

	public void setComCode(String comCode) {
		this.comCode = comCode;
	}

	public String getHandlerCode() {
		return this.handlerCode;
	}

	public void setHandlerCode(String handlerCode) {
		this.handlerCode = handlerCode;
	}

	public String getHandler1Code() {
		return this.handler1Code;
	}

	public void setHandler1Code(String handler1Code) {
		this.handler1Code = handler1Code;
	}

	public String getApproverCode() {
		return this.approverCode;
	}

	public void setApproverCode(String approverCode) {
		this.approverCode = approverCode;
	}

	public String getUnderWriteCode() {
		return this.underWriteCode;
	}

	public void setUnderWriteCode(String underWriteCode) {
		this.underWriteCode = underWriteCode;
	}

	public String getUnderWriteName() {
		return this.underWriteName;
	}

	public void setUnderWriteName(String underWriteName) {
		this.underWriteName = underWriteName;
	}

	public String getOperatorCode() {
		return this.operatorCode;
	}

	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}

	public Date getInputDate() {
		return this.inputDate;
	}

	public void setInputDate(Date inputDate) {
		this.inputDate = inputDate;
	}

	public BigDecimal getInputHour() {
		return this.inputHour;
	}

	public void setInputHour(BigDecimal inputHour) {
		this.inputHour = inputHour;
	}

	public Date getUnderWriteEndDate() {
		return this.underWriteEndDate;
	}

	public void setUnderWriteEndDate(Date underWriteEndDate) {
		this.underWriteEndDate = underWriteEndDate;
	}

	public Date getStatisticsYm() {
		return this.statisticsYm;
	}

	public void setStatisticsYm(Date statisticsYm) {
		this.statisticsYm = statisticsYm;
	}

	public String getAgentCode() {
		return this.agentCode;
	}

	public void setAgentCode(String agentCode) {
		this.agentCode = agentCode;
	}

	public String getCoinsFlag() {
		return this.coinsFlag;
	}

	public void setCoinsFlag(String coinsFlag) {
		this.coinsFlag = coinsFlag;
	}

	public String getReinsFlag() {
		return this.reinsFlag;
	}

	public void setReinsFlag(String reinsFlag) {
		this.reinsFlag = reinsFlag;
	}

	public String getAllinsFlag() {
		return this.allinsFlag;
	}

	public void setAllinsFlag(String allinsFlag) {
		this.allinsFlag = allinsFlag;
	}

	public String getUnderWriteFlag() {
		return this.underWriteFlag;
	}

	public void setUnderWriteFlag(String underWriteFlag) {
		this.underWriteFlag = underWriteFlag;
	}

	public String getOthFlag() {
		return this.othFlag;
	}

	public void setOthFlag(String othFlag) {
		this.othFlag = othFlag;
	}

	public String getValidFlag() {
		return this.validFlag;
	}

	public void setValidFlag(String validFlag) {
		this.validFlag = validFlag;
	}

	public String getJudicalCode() {
		return this.judicalCode;
	}

	public void setJudicalCode(String judicalCode) {
		this.judicalCode = judicalCode;
	}

	public BigDecimal getDisrate1() {
		return this.disrate1;
	}

	public void setDisrate1(BigDecimal disrate1) {
		this.disrate1 = disrate1;
	}

	public String getBusinessFlag() {
		return this.businessFlag;
	}

	public void setBusinessFlag(String businessFlag) {
		this.businessFlag = businessFlag;
	}

	public String getUpdaterCode() {
		return this.updaterCode;
	}

	public void setUpdaterCode(String updaterCode) {
		this.updaterCode = updaterCode;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateHour() {
		return this.updateHour;
	}

	public void setUpdateHour(String updateHour) {
		this.updateHour = updateHour;
	}

	public Date getSignDate() {
		return this.signDate;
	}

	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}

	public String getAgreementNo() {
		return this.agreementNo;
	}

	public void setAgreementNo(String agreementNo) {
		this.agreementNo = agreementNo;
	}

	public String getInquiryNo() {
		return this.inquiryNo;
	}

	public void setInquiryNo(String inquiryNo) {
		this.inquiryNo = inquiryNo;
	}

	public String getPayMode() {
		return this.payMode;
	}

	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}

	public String getShareHolderFlag() {
		return this.shareHolderFlag;
	}

	public void setShareHolderFlag(String shareHolderFlag) {
		this.shareHolderFlag = shareHolderFlag;
	}

	public String getVisaCode() {
		return this.visaCode;
	}

	public void setVisaCode(String visaCode) {
		this.visaCode = visaCode;
	}

	public String getManualType() {
		return this.manualType;
	}

	public void setManualType(String manualType) {
		this.manualType = manualType;
	}

	public String getNationFlag() {
		return this.nationFlag;
	}

	public void setNationFlag(String nationFlag) {
		this.nationFlag = nationFlag;
	}

	public String getJfeeFlag() {
		return this.jfeeFlag;
	}

	public void setJfeeFlag(String jfeeFlag) {
		this.jfeeFlag = jfeeFlag;
	}

	public Date getPrecheckDate() {
		return this.precheckDate;
	}

	public void setPrecheckDate(Date precheckDate) {
		this.precheckDate = precheckDate;
	}

	public String getHandlerName() {
		return this.handlerName;
	}

	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}

	public String getHandler1Name() {
		return this.handler1Name;
	}

	public void setHandler1Name(String handler1Name) {
		this.handler1Name = handler1Name;
	}

	public String getPayrefCode() {
		return this.payrefCode;
	}

	public void setPayrefCode(String payrefCode) {
		this.payrefCode = payrefCode;
	}

	public String getPayrefName() {
		return this.payrefName;
	}

	public void setPayrefName(String payrefName) {
		this.payrefName = payrefName;
	}

	public Date getPayrefTime() {
		return this.payrefTime;
	}

	public void setPayrefTime(Date payrefTime) {
		this.payrefTime = payrefTime;
	}

	public Date getPrintTime() {
		return this.printTime;
	}

	public void setPrintTime(Date printTime) {
		this.printTime = printTime;
	}

	public String getAgriNature() {
		return this.agriNature;
	}

	public void setAgriNature(String agriNature) {
		this.agriNature = agriNature;
	}

	public String getServiceProviders() {
		return this.serviceProviders;
	}

	public void setServiceProviders(String serviceProviders) {
		this.serviceProviders = serviceProviders;
	}

	public String getServiceType() {
		return this.serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getServices() {
		return this.services;
	}

	public void setServices(String services) {
		this.services = services;
	}

	public String getBillingWay() {
		return this.billingWay;
	}

	public void setBillingWay(String billingWay) {
		this.billingWay = billingWay;
	}

	public String getBusinessChannel() {
		return this.businessChannel;
	}

	public void setBusinessChannel(String businessChannel) {
		this.businessChannel = businessChannel;
	}

	public String getBusinessAgentType() {
		return this.businessAgentType;
	}

	public void setBusinessAgentType(String businessAgentType) {
		this.businessAgentType = businessAgentType;
	}

	public String getUniteAgentChannel() {
		return this.uniteAgentChannel;
	}

	public void setUniteAgentChannel(String uniteAgentChannel) {
		this.uniteAgentChannel = uniteAgentChannel;
	}

	public String getCurrency1() {
		return this.currency1;
	}

	public void setCurrency1(String currency1) {
		this.currency1 = currency1;
	}

	public String getCurrency2() {
		return this.currency2;
	}

	public void setCurrency2(String currency2) {
		this.currency2 = currency2;
	}

	public String getTwoAvoid() {
		return this.twoAvoid;
	}

	public void setTwoAvoid(String twoAvoid) {
		this.twoAvoid = twoAvoid;
	}

	public String getBusiNatureNo() {
		return this.busiNatureNo;
	}

	public void setBusiNatureNo(String busiNatureNo) {
		this.busiNatureNo = busiNatureNo;
	}

	public String getRenewal() {
		return this.renewal;
	}

	public void setRenewal(String renewal) {
		this.renewal = renewal;
	}

	public String getBusinessPlate() {
		return this.businessPlate;
	}

	public void setBusinessPlate(String businessPlate) {
		this.businessPlate = businessPlate;
	}

	public String getStockHolderCode() {
		return this.stockHolderCode;
	}

	public void setStockHolderCode(String stockHolderCode) {
		this.stockHolderCode = stockHolderCode;
	}

	public String getStockHolderName() {
		return this.stockHolderName;
	}

	public void setStockHolderName(String stockHolderName) {
		this.stockHolderName = stockHolderName;
	}

	public String getFarmFlag() {
		return this.farmFlag;
	}

	public void setFarmFlag(String farmFlag) {
		this.farmFlag = farmFlag;
	}

	public String getOtherBusinessFlag() {
		return this.otherBusinessFlag;
	}

	public void setOtherBusinessFlag(String otherBusinessFlag) {
		this.otherBusinessFlag = otherBusinessFlag;
	}

	public String getBusinessClass() {
		return this.businessClass;
	}

	public void setBusinessClass(String businessClass) {
		this.businessClass = businessClass;
	}

	public BigDecimal getPeccancyCoeff() {
		return this.peccancyCoeff;
	}

	public void setPeccancyCoeff(BigDecimal peccancyCoeff) {
		this.peccancyCoeff = peccancyCoeff;
	}

	public BigDecimal getClaimCoeff() {
		return this.claimCoeff;
	}

	public void setClaimCoeff(BigDecimal claimCoeff) {
		this.claimCoeff = claimCoeff;
	}

	public String getRateFloatFlag() {
		return this.rateFloatFlag;
	}

	public void setRateFloatFlag(String rateFloatFlag) {
		this.rateFloatFlag = rateFloatFlag;
	}

	public String getAgentName() {
		return this.agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getFlag() {
		return this.flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	
	/**
	 * @return 返回 endorseNo。
	 */
	public String getEndorseNo() {
		return endorseNo;
	}


	
	/**
	 * @param endorseNo 要设置的 endorseNo。
	 */
	public void setEndorseNo(String endorseNo) {
		this.endorseNo = endorseNo;
	}


	public List<PrpLCInsuredVo> getPrpCInsureds() {
		return this.prpCInsureds;
	}

	public void setPrpCInsureds(List<PrpLCInsuredVo> prpCInsureds) {
		this.prpCInsureds = prpCInsureds;
	}

	public List<PrpLCItemCarVo> getPrpCItemCars() {
		return this.prpCItemCars;
	}

	public void setPrpCItemCars(List<PrpLCItemCarVo> prpCItemCars) {
		this.prpCItemCars = prpCItemCars;
	}

	public List<PrpLCengageVo> getPrpCengages() {
		return this.prpCengages;
	}

	public void setPrpCengages(List<PrpLCengageVo> prpCengages) {
		this.prpCengages = prpCengages;
	}

	public List<PrpLCItemKindVo> getPrpCItemKinds() {
		return this.prpCItemKinds;
	}

	public void setPrpCItemKinds(List<PrpLCItemKindVo> prpCItemKinds) {
		this.prpCItemKinds = prpCItemKinds;
	}

	public String getValidNo() {
		return validNo;
	}

	public void setValidNo(String validNo) {
		this.validNo = validNo;
	}


	public List<PrpLcCarDeviceVo> getPrpLcCarDevices() {
		return prpLcCarDevices;
	}


	public void setPrpLcCarDevices(List<PrpLcCarDeviceVo> prpLcCarDevices) {
		this.prpLcCarDevices = prpLcCarDevices;
	}


	public String getIsCoreCustomer() {
		return isCoreCustomer;
	}

	public void setIsCoreCustomer(String isCoreCustomer) {
		this.isCoreCustomer = isCoreCustomer;
	}
	
	public String getIsTwentyFlag() {
		return isTwentyFlag;
	}


	public void setIsTwentyFlag(String isTwentyFlag) {
		this.isTwentyFlag = isTwentyFlag;
	}

}