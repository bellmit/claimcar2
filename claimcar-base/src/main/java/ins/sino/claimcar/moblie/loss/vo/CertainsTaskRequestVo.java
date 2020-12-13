package ins.sino.claimcar.moblie.loss.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("CERTAINSTASKINFO")
public class CertainsTaskRequestVo implements Serializable{
	private static final long serialVersionUID = -1568451102386689963L;
	@XStreamAlias("REGISTNO")
	private String registNo;  //报案号
	@XStreamAlias("CERTAINSID")
	private String certainsId;  //理赔定损ID
	@XStreamAlias("TASKID")
	private String taskId;  //任务id
	@XStreamAlias("NODETYPE")
	private String nodeType;  //调度节点
	@XStreamAlias("ITEMNO")
	private String itemNo;  //标的序号
	@XStreamAlias("ITEMNONAME")
	private String itemNoName;  //标的名称
	@XStreamAlias("ISOBJECT")
	private String isObject;  //是否标的
	@XStreamAlias("NEXTHANDLERCODE")
	private String nextHandlerCode;  //处理人代码
	@XStreamAlias("NEXTHANDLERNAME")
	private String nextHandlerName;  //处理人名称
	@XStreamAlias("SCHEDULEOBJECTID")
	private String scheduleObjectId;  //处理人员归属机构编码
	@XStreamAlias("SCHEDULEOBJECTNAME")
	private String scheduleObjectName;  //处理人员归属机构名称
	@XStreamAlias("OPTIONTYPE")
	private String optionType;  //操作类型
	@XStreamAlias("LICENSENO")
	private String licenseNo;  //车牌号码
	@XStreamAlias("LICENSETYPE")
	private String licenseType;  //车牌种类
	@XStreamAlias("BRANDNAME")
	private String brandName;  //厂牌型号
	@XStreamAlias("FRAMENO")
	private String frameNo;  //车架号
	@XStreamAlias("VIN")
	private String vin;  //VIN码
	@XStreamAlias("ENGINENO")
	private String engineNo;  //发动机号
	@XStreamAlias("VEHICLEMODLECODE")
	private String vehicleModleCode;  //车型编码
	@XStreamAlias("VEHICLEMODLENAME")
	private String vehicleModleName;  //车型名称
	@XStreamAlias("CARKINDCODE")
	private String carKindCode;  //车辆种类
	@XStreamAlias("OWERN")
	private String owern;  //车主
	@XStreamAlias("ENROLLDATE")
	private String enrollDate;  //初登日期
	@XStreamAlias("ASSESSIDENTIFYNO")
	private String assessIdentifyNo;  //定损员身份证号
	@XStreamAlias("ASSESSPLACE")
	private String assessPlace;  //定损地点
	@XStreamAlias("DRIVENAME")
	private String driveName;  //驾驶人姓名
	@XStreamAlias("DRIVINGLICENSENO")
	private String drivingLicenseNo;  //驾照号码
	@XStreamAlias("IDENTIFYTYPE")
	private String identifyType;  //驾驶人证件类型
	@XStreamAlias("IDENTIFYNO")
	private String identifyNo;  //驾驶人证件号码
	@XStreamAlias("STIPOLICYNO")
    private String stIpolicyNo;//交强险保单号
	@XStreamAlias("STINSURCOM")
    private String stInsurCom;//交强承保机构
	@XStreamAlias("INDEMNITYDUTY")
	private String indemnityDuty;  //事故责任
	@XStreamAlias("INDEMNITYDUTYRATE")
	private String indemnityDutyRate;  //事故责任比例
	@XStreamAlias("CIDUTYFLAG")
	private String ciDutyFlag;  //交强责任
	@XStreamAlias("LOSSOPINION")
	private String lossOpinion;  //定损意见
	@XStreamAlias("EXIGENCEGREE")
	private String exigenceGree;  //案件紧急程度
	@XStreamAlias("EXCESSTYPE")
	private String excessType;  //是否互碰自赔
	@XStreamAlias("VEHBRANDCODE")
	private String vehBrandCode;  //定损品牌编码
	@XStreamAlias("VEHGROUPCODE")
	private String vehGroupCode;  //车组编码
	@XStreamAlias("GROUPNAME")
	private String groupName;  //车组名称
	@XStreamAlias("SELFCONFIGFLAG")
	private String selfConfigFlag;  //自定义车型标志
	@XStreamAlias("SALVAGEFEE")
	private String salvageFee;  //定损施救费用
	@XStreamAlias("REMNANTFEE")
	private String remnantFee;  //定损折扣残值
	@XStreamAlias("MANAGEFEE")
	private String manageFee;  //定损管理费合计
	@XStreamAlias("EVALPARTSUM")
	private String evalPartSum;  //定损换件合计
	@XStreamAlias("EVALREPAIRSUM")
	private String evalRepairSum;  //定损工时合计
	@XStreamAlias("EVALMATESUM")
	private String evalMateSum;  //定损辅料合计
	@XStreamAlias("SELFPAYSUM")
	private String selfPaySum;  //自付合计
	@XStreamAlias("OUTERSUM")
	private String outerSum;  //定损外修合计
	@XStreamAlias("DEROGATIONSUM")
	private String derogationSum;  //减损合计
	@XStreamAlias("SUMLOSSAMOUNT")
	private String sumLossAmount;  //定损合计
	@XStreamAlias("HANDLERCODE")
	private String handlerCode;  //定损员代码
	@XStreamAlias("REMARK")
	private String remark;  //备注
	@XStreamAlias("PRICETYPE")
	private String priceType;  //价格类型
	@XStreamAlias("REPAIRFACID")
	private String repairfacId;  //修理厂ID
	@XStreamAlias("REPAIRFACCODE")
	private String repairfacCode;  //修理厂编码
	@XStreamAlias("REPAIRFACNAME")
	private String repairfacName;  //修理厂名称
	@XStreamAlias("PARTDISCOUNTPERCENT")
	private String partDiscountPercent;  //换件折扣
	@XStreamAlias("REPAIRDISCOUNTPERCENT")
	private String repairDiscountPercent;  //工时折扣
	@XStreamAlias("REPAIRFACTYPE")
	private String repairFacType;  //修理厂类型
	@XStreamAlias("SALVAGEFEEREMARK")
	private String salVageFeeRemark;  //施救费备注
	@XStreamAlias("SELFESTIFLAG")
	private String selfEstiFlag;  //自核价标记
	@XStreamAlias("EVALTYPECODE")
	private String evalTypeCode;  //定损方式
	@XStreamAlias("INSURANCECODE")
	private String insuranceCode;  //险别代码
	@XStreamAlias("INSURANCENAME")
	private String insuranceName;  //险别名称
	@XStreamAlias("LOSSFEETYPE")
	private String lossFeeType;  //损失类别
	@XStreamAlias("MIXCODE")
	private String mixCode;  //修理厂组织机构代码
	@XStreamAlias("REPAIRPHONE")
	private String repairPhone;  //修理厂手机
	@XStreamAlias("VIRTUALVALUE")
	private String virtualValue;  //总施救财产实际价值
	@XStreamAlias("VERSUMFITSFEE")
	private String verSumFitsFee;  //核损换金额合计
	@XStreamAlias("VERACCESSORFEE")
	private String verAccessorFee;  //核损辅料金额合计
	@XStreamAlias("VERSUMREPAIRFEE")
	private String verSumRepairFee;  //核损维修金额（工时费）合计
	@XStreamAlias("VERSUMSALVAGEFEE")
	private String verSumSalvageFee;  //核损残值合计
	@XStreamAlias("SUMVERIAMOUNT")
	private String sumVeriAmount;  //核损金额合计
	@XStreamAlias("SUMFEEAMOUNT")
	private String sumFeeAmount;  //费用合计
	@XStreamAlias("VEHKINDCODE")
	private String vehKindCode;  //定损车辆种类代码
	@XStreamAlias("VEHKINDNAME")
	private String vehKindName;  //定损车辆种类名称
	@XStreamAlias("VEHCERTAINCODE")
	private String vehCertainCode;  //定损车型编码
	@XStreamAlias("VEHCERTAINNAME")
	private String vehCertainName;  //定损车型名称
	@XStreamAlias("VEHSERICODE")
	private String vehSeriCode;  //定损车系编码
	@XStreamAlias("VEHSERINAME")
	private String vehSeriName;  //定损车系名称
	@XStreamAlias("VEHYEARTYPE")
	private String vehYearType;  //年款
	@XStreamAlias("VEHFACTORYCODE")
	private String vehFactoryCode;  //厂家编码
	@XStreamAlias("VEHFACTORYNAME")
	private String vehFactoryName;  //厂家名称
	@XStreamAlias("FROCEEXCLUSIONS")
	private String froceExclusions;//是否交强拒赔
	@XStreamAlias("BUSINESSEXCLUSIONS")
	private String businessExclusions;//是否商业拒赔
	@XStreamAlias("EXCLUSIONSREASON")
	private String exclusionsReason;//拒赔原因
	@XStreamAlias("DOCUMENTSCOMPLETE")
	private String documentsComplete;//是否单证齐全
	@XStreamAlias("ISSPECIALOPERATION")
	private String isspecialOperation;//是否有特种车操作证
	@XStreamAlias("ISBUSINESSQUALIFICATION")
	private String isbusinessQualification;//是否有营业车资格证
	@XStreamAlias("ISNORESPONSIBILITY")
	private String isnoResponsibility;//是否无责代赔OTHEREXCREASON
	@XStreamAlias("OTHEREXCREASON")
	private String otherexcReason;//其它拒赔原因	
	@XStreamAlias("ISGLASSBROKEN")
	private String isGlassBroken;  // 是否玻璃单独破碎
	@XStreamAlias("ISNOTFINDTHIRD")
	private String isNotFindThird;  // 是否属于无法找到第三方
	@XStreamAlias("COLLISIONLIST")
	private List<DefLossCollisionParts> defLossCollisionParts;//损失部位信息
	@XStreamAlias("PARTLIST")
	private List<DefLossPartInfoVo> defLossPartInfoVos;//换件信息
	@XStreamAlias("MANHOURLIST")
	private List<DefLossManhourInfoVo> defLossManhourInfoVos;//定损修理信息
	@XStreamAlias("OUTSIDEREPAIRTLIST")
	private List<DefLossOutRepairInfoVo> defLossOutRepairInfoVos;//定损外修信息
	@XStreamAlias("EVALREPAIRSUMLIST")
	private List<DeflossRepairSumVo> deflossRepairSumVos;//定损修理合计	
	@XStreamAlias("ASSISTLIST")
	private List<DefLossAssistInfoVo> defLossAssistInfoVo;//辅料信息
	@XStreamAlias("FEELIST")
	private List<DefLossFeeInfoVo> defLossfeeInfoVos;//费用信息
	public String getRegistNo() {
		return registNo;
	}
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	public String getCertainsId() {
		return certainsId;
	}
	public void setCertainsId(String certainsId) {
		this.certainsId = certainsId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getNodeType() {
		return nodeType;
	}
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	public String getItemNo() {
		return itemNo;
	}
	public void setItemNo(String itemNo) {
		this.itemNo = itemNo;
	}
	public String getItemNoName() {
		return itemNoName;
	}
	public void setItemNoName(String itemNoName) {
		this.itemNoName = itemNoName;
	}
	public String getIsObject() {
		return isObject;
	}
	public void setIsObject(String isObject) {
		this.isObject = isObject;
	}
	public String getNextHandlerCode() {
		return nextHandlerCode;
	}
	public void setNextHandlerCode(String nextHandlerCode) {
		this.nextHandlerCode = nextHandlerCode;
	}
	public String getNextHandlerName() {
		return nextHandlerName;
	}
	public void setNextHandlerName(String nextHandlerName) {
		this.nextHandlerName = nextHandlerName;
	}
	public String getScheduleObjectId() {
		return scheduleObjectId;
	}
	public void setScheduleObjectId(String scheduleObjectId) {
		this.scheduleObjectId = scheduleObjectId;
	}
	public String getScheduleObjectName() {
		return scheduleObjectName;
	}
	public void setScheduleObjectName(String scheduleObjectName) {
		this.scheduleObjectName = scheduleObjectName;
	}
	public String getOptionType() {
		return optionType;
	}
	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public String getLicenseType() {
		return licenseType;
	}
	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getFrameNo() {
		return frameNo;
	}
	public void setFrameNo(String frameNo) {
		this.frameNo = frameNo;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getEngineNo() {
		return engineNo;
	}
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}
	public String getVehicleModleCode() {
		return vehicleModleCode;
	}
	public void setVehicleModleCode(String vehicleModleCode) {
		this.vehicleModleCode = vehicleModleCode;
	}
	public String getVehicleModleName() {
		return vehicleModleName;
	}
	public void setVehicleModleName(String vehicleModleName) {
		this.vehicleModleName = vehicleModleName;
	}
	public String getCarKindCode() {
		return carKindCode;
	}
	public void setCarKindCode(String carKindCode) {
		this.carKindCode = carKindCode;
	}
	public String getOwern() {
		return owern;
	}
	public void setOwern(String owern) {
		this.owern = owern;
	}
	public String getEnrollDate() {
		return enrollDate;
	}
	public void setEnrollDate(String enrollDate) {
		this.enrollDate = enrollDate;
	}
	public String getAssessIdentifyNo() {
		return assessIdentifyNo;
	}
	public void setAssessIdentifyNo(String assessIdentifyNo) {
		this.assessIdentifyNo = assessIdentifyNo;
	}
	public String getAssessPlace() {
		return assessPlace;
	}
	public void setAssessPlace(String assessPlace) {
		this.assessPlace = assessPlace;
	}
	public String getDriveName() {
		return driveName;
	}
	public void setDriveName(String driveName) {
		this.driveName = driveName;
	}
	public String getDrivingLicenseNo() {
		return drivingLicenseNo;
	}
	public void setDrivingLicenseNo(String drivingLicenseNo) {
		this.drivingLicenseNo = drivingLicenseNo;
	}
	public String getIdentifyType() {
		return identifyType;
	}
	public void setIdentifyType(String identifyType) {
		this.identifyType = identifyType;
	}
	public String getIdentifyNo() {
		return identifyNo;
	}
	public void setIdentifyNo(String identifyNo) {
		this.identifyNo = identifyNo;
	}
	public String getStIpolicyNo() {
		return stIpolicyNo;
	}
	public void setStIpolicyNo(String stIpolicyNo) {
		this.stIpolicyNo = stIpolicyNo;
	}
	public String getStInsurCom() {
		return stInsurCom;
	}
	public void setStInsurCom(String stInsurCom) {
		this.stInsurCom = stInsurCom;
	}
	public String getIndemnityDuty() {
		return indemnityDuty;
	}
	public void setIndemnityDuty(String indemnityDuty) {
		this.indemnityDuty = indemnityDuty;
	}
	public String getIndemnityDutyRate() {
		return indemnityDutyRate;
	}
	public void setIndemnityDutyRate(String indemnityDutyRate) {
		this.indemnityDutyRate = indemnityDutyRate;
	}
	public String getCiDutyFlag() {
		return ciDutyFlag;
	}
	public void setCiDutyFlag(String ciDutyFlag) {
		this.ciDutyFlag = ciDutyFlag;
	}
	public String getLossOpinion() {
		return lossOpinion;
	}
	public void setLossOpinion(String lossOpinion) {
		this.lossOpinion = lossOpinion;
	}
	public String getExigenceGree() {
		return exigenceGree;
	}
	public void setExigenceGree(String exigenceGree) {
		this.exigenceGree = exigenceGree;
	}
	public String getExcessType() {
		return excessType;
	}
	public void setExcessType(String excessType) {
		this.excessType = excessType;
	}
	public String getVehBrandCode() {
		return vehBrandCode;
	}
	public void setVehBrandCode(String vehBrandCode) {
		this.vehBrandCode = vehBrandCode;
	}
	public String getVehGroupCode() {
		return vehGroupCode;
	}
	public void setVehGroupCode(String vehGroupCode) {
		this.vehGroupCode = vehGroupCode;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getSelfConfigFlag() {
		return selfConfigFlag;
	}
	public void setSelfConfigFlag(String selfConfigFlag) {
		this.selfConfigFlag = selfConfigFlag;
	}
	public String getSalvageFee() {
		return salvageFee;
	}
	public void setSalvageFee(String salvageFee) {
		this.salvageFee = salvageFee;
	}
	public String getRemnantFee() {
		return remnantFee;
	}
	public void setRemnantFee(String remnantFee) {
		this.remnantFee = remnantFee;
	}
	public String getManageFee() {
		return manageFee;
	}
	public void setManageFee(String manageFee) {
		this.manageFee = manageFee;
	}
	public String getEvalPartSum() {
		return evalPartSum;
	}
	public void setEvalPartSum(String evalPartSum) {
		this.evalPartSum = evalPartSum;
	}
	public String getEvalRepairSum() {
		return evalRepairSum;
	}
	public void setEvalRepairSum(String evalRepairSum) {
		this.evalRepairSum = evalRepairSum;
	}
	public String getEvalMateSum() {
		return evalMateSum;
	}
	public void setEvalMateSum(String evalMateSum) {
		this.evalMateSum = evalMateSum;
	}
	public String getSelfPaySum() {
		return selfPaySum;
	}
	public void setSelfPaySum(String selfPaySum) {
		this.selfPaySum = selfPaySum;
	}
	public String getOuterSum() {
		return outerSum;
	}
	public void setOuterSum(String outerSum) {
		this.outerSum = outerSum;
	}
	public String getDerogationSum() {
		return derogationSum;
	}
	public void setDerogationSum(String derogationSum) {
		this.derogationSum = derogationSum;
	}
	public String getSumLossAmount() {
		return sumLossAmount;
	}
	public void setSumLossAmount(String sumLossAmount) {
		this.sumLossAmount = sumLossAmount;
	}
	public String getHandlerCode() {
		return handlerCode;
	}
	public void setHandlerCode(String handlerCode) {
		this.handlerCode = handlerCode;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPriceType() {
		return priceType;
	}
	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}
	public String getRepairfacId() {
		return repairfacId;
	}
	public void setRepairfacId(String repairfacId) {
		this.repairfacId = repairfacId;
	}
	public String getRepairfacCode() {
		return repairfacCode;
	}
	public void setRepairfacCode(String repairfacCode) {
		this.repairfacCode = repairfacCode;
	}
	public String getRepairfacName() {
		return repairfacName;
	}
	public void setRepairfacName(String repairfacName) {
		this.repairfacName = repairfacName;
	}
	public String getPartDiscountPercent() {
		return partDiscountPercent;
	}
	public void setPartDiscountPercent(String partDiscountPercent) {
		this.partDiscountPercent = partDiscountPercent;
	}
	public String getRepairDiscountPercent() {
		return repairDiscountPercent;
	}
	public void setRepairDiscountPercent(String repairDiscountPercent) {
		this.repairDiscountPercent = repairDiscountPercent;
	}
	public String getRepairFacType() {
		return repairFacType;
	}
	public void setRepairFacType(String repairFacType) {
		this.repairFacType = repairFacType;
	}
	public String getSalVageFeeRemark() {
		return salVageFeeRemark;
	}
	public void setSalVageFeeRemark(String salVageFeeRemark) {
		this.salVageFeeRemark = salVageFeeRemark;
	}
	public String getSelfEstiFlag() {
		return selfEstiFlag;
	}
	public void setSelfEstiFlag(String selfEstiFlag) {
		this.selfEstiFlag = selfEstiFlag;
	}
	public String getEvalTypeCode() {
		return evalTypeCode;
	}
	public void setEvalTypeCode(String evalTypeCode) {
		this.evalTypeCode = evalTypeCode;
	}
	public String getInsuranceCode() {
		return insuranceCode;
	}
	public void setInsuranceCode(String insuranceCode) {
		this.insuranceCode = insuranceCode;
	}
	public String getInsuranceName() {
		return insuranceName;
	}
	public void setInsuranceName(String insuranceName) {
		this.insuranceName = insuranceName;
	}
	public String getLossFeeType() {
		return lossFeeType;
	}
	public void setLossFeeType(String lossFeeType) {
		this.lossFeeType = lossFeeType;
	}
	public String getMixCode() {
		return mixCode;
	}
	public void setMixCode(String mixCode) {
		this.mixCode = mixCode;
	}
	public String getRepairPhone() {
		return repairPhone;
	}
	public void setRepairPhone(String repairPhone) {
		this.repairPhone = repairPhone;
	}
	public String getVirtualValue() {
		return virtualValue;
	}
	public void setVirtualValue(String virtualValue) {
		this.virtualValue = virtualValue;
	}
	public String getVerSumFitsFee() {
		return verSumFitsFee;
	}
	public void setVerSumFitsFee(String verSumFitsFee) {
		this.verSumFitsFee = verSumFitsFee;
	}
	public String getVerAccessorFee() {
		return verAccessorFee;
	}
	public void setVerAccessorFee(String verAccessorFee) {
		this.verAccessorFee = verAccessorFee;
	}
	public String getVerSumRepairFee() {
		return verSumRepairFee;
	}
	public void setVerSumRepairFee(String verSumRepairFee) {
		this.verSumRepairFee = verSumRepairFee;
	}
	public String getVerSumSalvageFee() {
		return verSumSalvageFee;
	}
	public void setVerSumSalvageFee(String verSumSalvageFee) {
		this.verSumSalvageFee = verSumSalvageFee;
	}
	public String getSumVeriAmount() {
		return sumVeriAmount;
	}
	public void setSumVeriAmount(String sumVeriAmount) {
		this.sumVeriAmount = sumVeriAmount;
	}
	public String getSumFeeAmount() {
		return sumFeeAmount;
	}
	public void setSumFeeAmount(String sumFeeAmount) {
		this.sumFeeAmount = sumFeeAmount;
	}
	public String getVehKindCode() {
		return vehKindCode;
	}
	public void setVehKindCode(String vehKindCode) {
		this.vehKindCode = vehKindCode;
	}
	public String getVehKindName() {
		return vehKindName;
	}
	public void setVehKindName(String vehKindName) {
		this.vehKindName = vehKindName;
	}
	public String getVehCertainCode() {
		return vehCertainCode;
	}
	public void setVehCertainCode(String vehCertainCode) {
		this.vehCertainCode = vehCertainCode;
	}
	public String getVehCertainName() {
		return vehCertainName;
	}
	public void setVehCertainName(String vehCertainName) {
		this.vehCertainName = vehCertainName;
	}
	public String getVehSeriCode() {
		return vehSeriCode;
	}
	public void setVehSeriCode(String vehSeriCode) {
		this.vehSeriCode = vehSeriCode;
	}
	public String getVehSeriName() {
		return vehSeriName;
	}
	public void setVehSeriName(String vehSeriName) {
		this.vehSeriName = vehSeriName;
	}
	public String getVehYearType() {
		return vehYearType;
	}
	public void setVehYearType(String vehYearType) {
		this.vehYearType = vehYearType;
	}
	public String getVehFactoryCode() {
		return vehFactoryCode;
	}
	public void setVehFactoryCode(String vehFactoryCode) {
		this.vehFactoryCode = vehFactoryCode;
	}
	public String getVehFactoryName() {
		return vehFactoryName;
	}
	public void setVehFactoryName(String vehFactoryName) {
		this.vehFactoryName = vehFactoryName;
	}
	public String getFroceExclusions() {
		return froceExclusions;
	}
	public void setFroceExclusions(String froceExclusions) {
		this.froceExclusions = froceExclusions;
	}
	public String getBusinessExclusions() {
		return businessExclusions;
	}
	public void setBusinessExclusions(String businessExclusions) {
		this.businessExclusions = businessExclusions;
	}
	public String getExclusionsReason() {
		return exclusionsReason;
	}
	public void setExclusionsReason(String exclusionsReason) {
		this.exclusionsReason = exclusionsReason;
	}
	public String getDocumentsComplete() {
		return documentsComplete;
	}
	public void setDocumentsComplete(String documentsComplete) {
		this.documentsComplete = documentsComplete;
	}
	public String getIsspecialOperation() {
		return isspecialOperation;
	}
	public void setIsspecialOperation(String isspecialOperation) {
		this.isspecialOperation = isspecialOperation;
	}
	public String getIsbusinessQualification() {
		return isbusinessQualification;
	}
	public void setIsbusinessQualification(String isbusinessQualification) {
		this.isbusinessQualification = isbusinessQualification;
	}
	public String getIsnoResponsibility() {
		return isnoResponsibility;
	}
	public void setIsnoResponsibility(String isnoResponsibility) {
		this.isnoResponsibility = isnoResponsibility;
	}
	public String getOtherexcReason() {
		return otherexcReason;
	}
	public void setOtherexcReason(String otherexcReason) {
		this.otherexcReason = otherexcReason;
	}
	public List<DefLossCollisionParts> getDefLossCollisionParts() {
		return defLossCollisionParts;
	}
	public void setDefLossCollisionParts(
			List<DefLossCollisionParts> defLossCollisionParts) {
		this.defLossCollisionParts = defLossCollisionParts;
	}
	public List<DefLossPartInfoVo> getDefLossPartInfoVos() {
		return defLossPartInfoVos;
	}
	public void setDefLossPartInfoVos(List<DefLossPartInfoVo> defLossPartInfoVos) {
		this.defLossPartInfoVos = defLossPartInfoVos;
	}
	public List<DefLossManhourInfoVo> getDefLossManhourInfoVos() {
		return defLossManhourInfoVos;
	}
	public void setDefLossManhourInfoVos(
			List<DefLossManhourInfoVo> defLossManhourInfoVos) {
		this.defLossManhourInfoVos = defLossManhourInfoVos;
	}
	public List<DefLossOutRepairInfoVo> getDefLossOutRepairInfoVos() {
		return defLossOutRepairInfoVos;
	}
	public void setDefLossOutRepairInfoVos(
			List<DefLossOutRepairInfoVo> defLossOutRepairInfoVos) {
		this.defLossOutRepairInfoVos = defLossOutRepairInfoVos;
	}
	public List<DeflossRepairSumVo> getDeflossRepairSumVos() {
		return deflossRepairSumVos;
	}
	public void setDeflossRepairSumVos(List<DeflossRepairSumVo> deflossRepairSumVos) {
		this.deflossRepairSumVos = deflossRepairSumVos;
	}
	public List<DefLossAssistInfoVo> getDefLossAssistInfoVo() {
		return defLossAssistInfoVo;
	}
	public void setDefLossAssistInfoVo(List<DefLossAssistInfoVo> defLossAssistInfoVo) {
		this.defLossAssistInfoVo = defLossAssistInfoVo;
	}
	public List<DefLossFeeInfoVo> getDefLossfeeInfoVos() {
		return defLossfeeInfoVos;
	}
	public void setDefLossfeeInfoVos(List<DefLossFeeInfoVo> defLossfeeInfoVos) {
		this.defLossfeeInfoVos = defLossfeeInfoVos;
	}
	public String getIsGlassBroken() {
		return isGlassBroken;
	}
	public void setIsGlassBroken(String isGlassBroken) {
		this.isGlassBroken = isGlassBroken;
	}
	public String getIsNotFindThird() {
		return isNotFindThird;
	}
	public void setIsNotFindThird(String isNotFindThird) {
		this.isNotFindThird = isNotFindThird;
	}
	
}
