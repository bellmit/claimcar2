/******************************************************************************
 * CREATETIME : 2016年6月1日 下午3:48:09
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapterSH;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 车辆损失情况（多条）
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHBIEndCaseAddReqVehicleDataVo {

	/**
	 * 车辆损失部位（多条）LOSS_PART_LIST（隶属于车辆损失情况）
	 */
	@XmlElementWrapper(name = "LOSS_PART_LIST")
	@XmlElement(name = "LOSS_PART_DATA")
	private List<SHCIEndCaseAddReqCarLossPartDataVo> carLossPartDataVo;// 车辆损失部位（多条）

	/**
	 * 车辆配件明细（多条）FITTING_LIST（隶属于车辆损失情况）
	 */
	@XmlElementWrapper(name = "FITTING_LIST")
	@XmlElement(name = "FITTING_DATA")
	private List<SHCIEndCaseAddReqFittingDataVo> fittingDataVo;// 车辆配件明细（多条）

	/**
	 * 开票修理机构（多条）REPAIR_LIST（隶属于车辆损失情况）
	 */
	@XmlElementWrapper(name = "REPAIR_LIST")
	@XmlElement(name = "REPAIR_DATA")
	private List<SHCIEndCaseAddReqRepairDataVo> repairDataVo;// 开票修理机构（多条）

	/**
	 * 承修修理机构（多条）ActuralRepairList（隶属于车辆损失情况）
	 */
	@XmlElementWrapper(name = "ActuralRepairList")
	@XmlElement(name = "ActuralRepairData")
	private List<SHCIEndCaseAddReqActuralRepairDataVo> acturalRepairDataVo;// 承修修理机构（多条）

	@XmlElement(name = "CAR_MARK")
	private String carMark;// 损失车辆号牌号码

	@XmlElement(name = "VEHICLE_TYPE")
	private String vehicleType;// 损失车辆号牌种类，《公用代码》1.1.2

	@XmlElement(name = "LOSS_AMOUNT", required = true)
	private Double lossAmount;// 损失金额，指实际损失金额

	@XmlElement(name = "MAIN_THIRD", required = true)
	private String mainThird;// 是否承保车辆，《公用代码》1.1.52

	@XmlElement(name = "ROBBER")
	private String robber;// 是否盗抢，《公用代码》1.1.52

	@XmlElement(name = "SURVEY_TYPE")
	private String surveyType;// 现场类别，《公用代码》1.1.77

	@XmlElement(name = "SURVEY_NAME")
	private String surveyName;// 查勘人员姓名

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "SURVEY_START_TIME")
	private Date surveyStartTime;// 查勘开始时间，YYYYMMDDHHMMSS

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "SURVEY_END_TIME")
	private Date surveyEndTime;// 查勘结束时间

	@XmlElement(name = "SURVEY_PLACE")
	private String surveyPlace;// 查勘地点

	@XmlElement(name = "SURVEY_DES")
	private String surveyDes;// 查勘情况说明，事故具体描述

	@XmlElement(name = "ESTIMATE_NAME", required = true)
	private String estimateName;// 定损人员代码，《公用代码》1.1.96

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "ESTIMATE_START_TIME")
	private Date estimateStartTime;// 定损开始时间，YYYYMMDDHHMMSS

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "ESTIMATE_END_TIME")
	private Date estimateEndTime;// 定损结束时间，YYYYMMDDHHMMSS

	@XmlElement(name = "ASSESOR_NAME")
	private String assesorName;// 核损人员姓名

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "ASSESOR_START_TIME")
	private Date assesorStartTime;// 核损开始时间，YYYYMMDDHHMMSS

	@XmlJavaTypeAdapter(DateXmlAdapterSH.class)
	@XmlElement(name = "ASSESOR_END_TIME")
	private Date assesorEndTime;// 核损结束时间，YYYYMMDDHHMMSS

	@XmlElement(name = "REMNANT")
	private Double remnant;// 残值，保险公司预收回的残值的预估金额

	@XmlElement(name = "CHARGE_FEE")
	private Double charg4eFee;// 管理费用，经销差率

	@XmlElement(name = "TOTAL_WORKING_HOUR")
	private Double totalWorkingHour;// 配件总工时

	@XmlElement(name = "CHANGE_OR_REPAIR_PART", required = true)
	private String changeOrRepairPart;// 是否有修理或更换配件

	@XmlElement(name = "JY_VEHICLE_CODE")
	private String jyVehicleCode;// 精友车型代码

	@XmlElement(name = "CLAIM_VEHICLE_NAME")
	private String claimVehicleName;// 理赔车型名称

	@XmlElement(name = "CLAIM_VEHICLE_CODE")
	private String claimVehicleCode;// 理赔车型代码

	@XmlElement(name = "MADE_FACTORY", required = true)
	private String madeFactory;// 制造厂名称

	@XmlElement(name = "VEHICLE_BRAND_CODE")
	private String vehicleBrandCode;// 品牌代码

	@XmlElement(name = "VEHICLE_CATENA_CODE")
	private String vehicleCatenaCode;// 车系代码

	@XmlElement(name = "VEHICLE_GROUP_CODE")
	private String vehicleGroupCode;// 车组代码

	@XmlElement(name = "PRICE_SLT_CODE", required = true)
	private String priceSltCode;// 价格方案代码，《公用代码》1.1.99

	@XmlElement(name = "DEFINE_FLAG", required = true)
	private String defineFlag;// 定义标志，《公用代码》1.1.100

	@XmlElement(name = "VIN")
	private String vin;// 损失车辆VIN码

	@XmlElement(name = "EngineNo")
	private String engineNo;// 损失车辆发动机号

	@XmlElement(name = "Model")
	private String model;// 损失车辆厂牌型号

	@XmlElement(name = "DriverName", required = true)
	private String driverName;// 出险车辆驾驶员姓名

	@XmlElement(name = "CertiType", required = true)
	private String certiType;// 出险车辆驾驶员证件类型，《公用代码》1.1.17

	@XmlElement(name = "CertiCode", required = true)
	private String certiCode;// 出险车辆驾驶员证件号码

	@XmlElement(name = "DriverLicenseNo", required = true)
	private String driverLicenseNo;// 出险车辆驾驶员档案编号

	@XmlElement(name = "TemporaryFlag", required = true)
	private String TemporaryFlag;// 是否临时车辆
	
	/* 牛强 2017-03-15 改 */
	@XmlElement(name = "CheckerCode")
	private String checkerCode;// 查勘人员代码 CheckerCertiCode

	@XmlElement(name = "CheckerCertiCode")
	private String checkerCertiCode;// 查勘人员身份证

	@XmlElement(name = "UnderWriteCode")
	private String underWriteCode;// 核损人员代码
	
	@XmlElement(name = "UnderWriteCertiCode")
	private String underWriteCertiCode;//核损人员身份证号
	
	@XmlElement(name = "UnderDefLoss")
	private double underDefLoss;// 核损金额

	@XmlElement(name = "IsTotalLoss")
	private String isTotalLoss; // 是否全损

	@XmlElement(name = "IsHotSinceDetonation")
	private String isHotSinceDetonation; // 是否火自爆

	@XmlElement(name = "IsWaterFlooded")
	private String isWaterFlooded; // 是否水淹

	@XmlElement(name = "WaterFloodedLevel")
	private String waterFloodedLevel; // 水淹等级
	
	@XmlElement(name = "IsGlassBroken")
	private String isGlassBroken;  // 是否玻璃单独破碎
	
	@XmlElement(name = "IsNotFindThird")
	private String isNotFindThird;  // 是否属于无法找到第三方

	/**
	 * @return 返回 carMark 损失车辆号牌号码
	 */
	public String getCarMark() {
		return carMark;
	}

	/**
	 * @param carMark 要设置的 损失车辆号牌号码
	 */
	public void setCarMark(String carMark) {
		this.carMark = carMark;
	}

	/**
	 * @return 返回 vehicleType 损失车辆号牌种类，《公用代码》1.1.2
	 */
	public String getVehicleType() {
		return vehicleType;
	}

	/**
	 * @param vehicleType 要设置的 损失车辆号牌种类，《公用代码》1.1.2
	 */
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	/**
	 * @return 返回 lossAmount 损失金额，指实际损失金额
	 */
	public Double getLossAmount() {
		return lossAmount;
	}

	/**
	 * @param lossAmount 要设置的 损失金额，指实际损失金额
	 */
	public void setLossAmount(Double lossAmount) {
		this.lossAmount = lossAmount;
	}

	/**
	 * @return 返回 mainThird 是否承保车辆，《公用代码》1.1.52
	 */
	public String getMainThird() {
		return mainThird;
	}

	/**
	 * @param mainThird 要设置的 是否承保车辆，《公用代码》1.1.52
	 */
	public void setMainThird(String mainThird) {
		this.mainThird = mainThird;
	}

	/**
	 * @return 返回 robber 是否盗抢，《公用代码》1.1.52
	 */
	public String getRobber() {
		return robber;
	}

	/**
	 * @param robber 要设置的 是否盗抢，《公用代码》1.1.52
	 */
	public void setRobber(String robber) {
		this.robber = robber;
	}

	/**
	 * @return 返回 surveyType 现场类别，《公用代码》1.1.77
	 */
	public String getSurveyType() {
		return surveyType;
	}

	/**
	 * @param surveyType 要设置的 现场类别，《公用代码》1.1.77
	 */
	public void setSurveyType(String surveyType) {
		this.surveyType = surveyType;
	}

	/**
	 * @return 返回 surveyName 查勘人员姓名
	 */
	public String getSurveyName() {
		return surveyName;
	}

	/**
	 * @param surveyName 要设置的 查勘人员姓名
	 */
	public void setSurveyName(String surveyName) {
		this.surveyName = surveyName;
	}

	/**
	 * @return 返回 surveyStartTime 查勘开始时间，YYYYMMDDHHMMSS
	 */
	public Date getSurveyStartTime() {
		return surveyStartTime;
	}

	/**
	 * @param surveyStartTime 要设置的 查勘开始时间，YYYYMMDDHHMMSS
	 */
	public void setSurveyStartTime(Date surveyStartTime) {
		this.surveyStartTime = surveyStartTime;
	}

	/**
	 * @return 返回 surveyEndTime 查勘结束时间
	 */
	public Date getSurveyEndTime() {
		return surveyEndTime;
	}

	/**
	 * @param surveyEndTime 要设置的 查勘结束时间
	 */
	public void setSurveyEndTime(Date surveyEndTime) {
		this.surveyEndTime = surveyEndTime;
	}

	/**
	 * @return 返回 surveyPlace 查勘地点
	 */
	public String getSurveyPlace() {
		return surveyPlace;
	}

	/**
	 * @param surveyPlace 要设置的 查勘地点
	 */
	public void setSurveyPlace(String surveyPlace) {
		this.surveyPlace = surveyPlace;
	}

	/**
	 * @return 返回 surveyDes 查勘情况说明，事故具体描述
	 */
	public String getSurveyDes() {
		return surveyDes;
	}

	/**
	 * @param surveyDes 要设置的 查勘情况说明，事故具体描述
	 */
	public void setSurveyDes(String surveyDes) {
		this.surveyDes = surveyDes;
	}

	/**
	 * @return 返回 estimateName 定损人员代码，《公用代码》1.1.96
	 */
	public String getEstimateName() {
		return estimateName;
	}

	/**
	 * @param estimateName 要设置的 定损人员代码，《公用代码》1.1.96
	 */
	public void setEstimateName(String estimateName) {
		this.estimateName = estimateName;
	}

	/**
	 * @return 返回 estimateStartTime 定损开始时间，YYYYMMDDHHMMSS
	 */
	public Date getEstimateStartTime() {
		return estimateStartTime;
	}

	/**
	 * @param estimateStartTime 要设置的 定损开始时间，YYYYMMDDHHMMSS
	 */
	public void setEstimateStartTime(Date estimateStartTime) {
		this.estimateStartTime = estimateStartTime;
	}

	/**
	 * @return 返回 estimateEndTime 定损结束时间，YYYYMMDDHHMMSS
	 */
	public Date getEstimateEndTime() {
		return estimateEndTime;
	}

	/**
	 * @param estimateEndTime 要设置的 定损结束时间，YYYYMMDDHHMMSS
	 */
	public void setEstimateEndTime(Date estimateEndTime) {
		this.estimateEndTime = estimateEndTime;
	}

	/**
	 * @return 返回 assesorName 核损人员姓名
	 */
	public String getAssesorName() {
		return assesorName;
	}

	/**
	 * @param assesorName 要设置的 核损人员姓名
	 */
	public void setAssesorName(String assesorName) {
		this.assesorName = assesorName;
	}

	/**
	 * @return 返回 assesorStartTime 核损开始时间，YYYYMMDDHHMMSS
	 */
	public Date getAssesorStartTime() {
		return assesorStartTime;
	}

	/**
	 * @param assesorStartTime 要设置的 核损开始时间，YYYYMMDDHHMMSS
	 */
	public void setAssesorStartTime(Date assesorStartTime) {
		this.assesorStartTime = assesorStartTime;
	}

	/**
	 * @return 返回 assesorEndTime 核损结束时间，YYYYMMDDHHMMSS
	 */
	public Date getAssesorEndTime() {
		return assesorEndTime;
	}

	/**
	 * @param assesorEndTime 要设置的 核损结束时间，YYYYMMDDHHMMSS
	 */
	public void setAssesorEndTime(Date assesorEndTime) {
		this.assesorEndTime = assesorEndTime;
	}

	/**
	 * @return 返回 remnant 残值，保险公司预收回的残值的预估金额
	 */
	public Double getRemnant() {
		return remnant;
	}

	/**
	 * @param remnant 要设置的 残值，保险公司预收回的残值的预估金额
	 */
	public void setRemnant(Double remnant) {
		this.remnant = remnant;
	}

	/**
	 * @return 返回 charg 4eFee 管理费用，经销差率
	 */
	public Double getCharg4eFee() {
		return charg4eFee;
	}

	/**
	 * @param charg 4eFee 要设置的 管理费用，经销差率
	 */
	public void setCharg4eFee(Double charg4eFee) {
		this.charg4eFee = charg4eFee;
	}

	/**
	 * @return 返回 totalWorkingHour 配件总工时
	 */
	public Double getTotalWorkingHour() {
		return totalWorkingHour;
	}

	/**
	 * @param totalWorkingHour 要设置的 配件总工时
	 */
	public void setTotalWorkingHour(Double totalWorkingHour) {
		this.totalWorkingHour = totalWorkingHour;
	}

	/**
	 * @return 返回 changeOrRepairPart 是否有修理或更换配件
	 */
	public String getChangeOrRepairPart() {
		return changeOrRepairPart;
	}

	/**
	 * @param changeOrRepairPart 要设置的 是否有修理或更换配件
	 */
	public void setChangeOrRepairPart(String changeOrRepairPart) {
		this.changeOrRepairPart = changeOrRepairPart;
	}

	/**
	 * @return 返回 jyVehicleCode 精友车型代码
	 */
	public String getJyVehicleCode() {
		return jyVehicleCode;
	}

	/**
	 * @param jyVehicleCode 要设置的 精友车型代码
	 */
	public void setJyVehicleCode(String jyVehicleCode) {
		this.jyVehicleCode = jyVehicleCode;
	}

	/**
	 * @return 返回 claimVehicleName 理赔车型名称
	 */
	public String getClaimVehicleName() {
		return claimVehicleName;
	}

	/**
	 * @param claimVehicleName 要设置的 理赔车型名称
	 */
	public void setClaimVehicleName(String claimVehicleName) {
		this.claimVehicleName = claimVehicleName;
	}

	/**
	 * @return 返回 claimVehicleCode 理赔车型代码
	 */
	public String getClaimVehicleCode() {
		return claimVehicleCode;
	}

	/**
	 * @param claimVehicleCode 要设置的 理赔车型代码
	 */
	public void setClaimVehicleCode(String claimVehicleCode) {
		this.claimVehicleCode = claimVehicleCode;
	}

	/**
	 * @return 返回 madeFactory 制造厂名称
	 */
	public String getMadeFactory() {
		return madeFactory;
	}

	/**
	 * @param madeFactory 要设置的 制造厂名称
	 */
	public void setMadeFactory(String madeFactory) {
		this.madeFactory = madeFactory;
	}

	/**
	 * @return 返回 vehicleBrandCode 品牌代码
	 */
	public String getVehicleBrandCode() {
		return vehicleBrandCode;
	}

	/**
	 * @param vehicleBrandCode 要设置的 品牌代码
	 */
	public void setVehicleBrandCode(String vehicleBrandCode) {
		this.vehicleBrandCode = vehicleBrandCode;
	}

	/**
	 * @return 返回 vehicleCatenaCode 车系代码
	 */
	public String getVehicleCatenaCode() {
		return vehicleCatenaCode;
	}

	/**
	 * @param vehicleCatenaCode 要设置的 车系代码
	 */
	public void setVehicleCatenaCode(String vehicleCatenaCode) {
		this.vehicleCatenaCode = vehicleCatenaCode;
	}

	/**
	 * @return 返回 vehicleGroupCode 车组代码
	 */
	public String getVehicleGroupCode() {
		return vehicleGroupCode;
	}

	/**
	 * @param vehicleGroupCode 要设置的 车组代码
	 */
	public void setVehicleGroupCode(String vehicleGroupCode) {
		this.vehicleGroupCode = vehicleGroupCode;
	}

	/**
	 * @return 返回 priceSltCode 价格方案代码，《公用代码》1.1.99
	 */
	public String getPriceSltCode() {
		return priceSltCode;
	}

	/**
	 * @param priceSltCode 要设置的 价格方案代码，《公用代码》1.1.99
	 */
	public void setPriceSltCode(String priceSltCode) {
		this.priceSltCode = priceSltCode;
	}

	/**
	 * @return 返回 defineFlag 定义标志，《公用代码》1.1.100
	 */
	public String getDefineFlag() {
		return defineFlag;
	}

	/**
	 * @param defineFlag 要设置的 定义标志，《公用代码》1.1.100
	 */
	public void setDefineFlag(String defineFlag) {
		this.defineFlag = defineFlag;
	}

	/**
	 * @return 返回 vin 损失车辆VIN码
	 */
	public String getVin() {
		return vin;
	}

	/**
	 * @param vin 要设置的 损失车辆VIN码
	 */
	public void setVin(String vin) {
		this.vin = vin;
	}

	/**
	 * @return 返回 engineno 损失车辆发动机号
	 */
	public String getEngineNo() {
		return engineNo;
	}

	/**
	 * @param engineno 要设置的 损失车辆发动机号
	 */
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	/**
	 * @return 返回 model 损失车辆厂牌型号
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @param model 要设置的 损失车辆厂牌型号
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * @return 返回 drivername 出险车辆驾驶员姓名
	 */
	public String getDriverName() {
		return driverName;
	}

	/**
	 * @param drivername 要设置的 出险车辆驾驶员姓名
	 */
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	/**
	 * @return 返回 certitype 出险车辆驾驶员证件类型，《公用代码》1.1.17
	 */
	public String getCertiType() {
		return certiType;
	}

	/**
	 * @param certitype 要设置的 出险车辆驾驶员证件类型，《公用代码》1.1.17
	 */
	public void setCertiType(String certiType) {
		this.certiType = certiType;
	}

	/**
	 * @return 返回 certicode 出险车辆驾驶员证件号码
	 */
	public String getCertiCode() {
		return certiCode;
	}

	/**
	 * @param certicode 要设置的 出险车辆驾驶员证件号码
	 */
	public void setCertiCode(String certiCode) {
		this.certiCode = certiCode;
	}

	/**
	 * @return 返回 driverlicenseno 出险车辆驾驶员档案编号
	 */
	public String getDriverLicenseNo() {
		return driverLicenseNo;
	}

	/**
	 * @param driverlicenseno 要设置的 出险车辆驾驶员档案编号
	 */
	public void setDriverLicenseNo(String driverLicenseNo) {
		this.driverLicenseNo = driverLicenseNo;
	}

	public List<SHCIEndCaseAddReqCarLossPartDataVo> getCarLossPartDataVo() {
		return carLossPartDataVo;
	}

	public void setCarLossPartDataVo(List<SHCIEndCaseAddReqCarLossPartDataVo> carLossPartDataVo) {
		this.carLossPartDataVo = carLossPartDataVo;
	}

	public List<SHCIEndCaseAddReqFittingDataVo> getFittingDataVo() {
		return fittingDataVo;
	}

	public void setFittingDataVo(List<SHCIEndCaseAddReqFittingDataVo> fittingDataVo) {
		this.fittingDataVo = fittingDataVo;
	}

	public List<SHCIEndCaseAddReqRepairDataVo> getRepairDataVo() {
		return repairDataVo;
	}

	public void setRepairDataVo(List<SHCIEndCaseAddReqRepairDataVo> repairDataVo) {
		this.repairDataVo = repairDataVo;
	}

	public List<SHCIEndCaseAddReqActuralRepairDataVo> getActuralRepairDataVo() {
		return acturalRepairDataVo;
	}

	public void setActuralRepairDataVo(List<SHCIEndCaseAddReqActuralRepairDataVo> acturalRepairDataVo) {
		this.acturalRepairDataVo = acturalRepairDataVo;
	}

	public String getTemporaryFlag() {
		return TemporaryFlag;
	}

	public void setTemporaryFlag(String temporaryFlag) {
		TemporaryFlag = temporaryFlag;
	}

	public String getCheckerCode() {
		return checkerCode;
	}

	public void setCheckerCode(String checkerCode) {
		this.checkerCode = checkerCode;
	}

	public String getCheckerCertiCode() {
		return checkerCertiCode;
	}

	public void setCheckerCertiCode(String checkerCertiCode) {
		this.checkerCertiCode = checkerCertiCode;
	}

	public String getUnderWriteCode() {
		return underWriteCode;
	}

	public void setUnderWriteCode(String underWriteCode) {
		this.underWriteCode = underWriteCode;
	}

	public String getUnderWriteCertiCode() {
		return underWriteCertiCode;
	}

	public void setUnderWriteCertiCode(String underWriteCertiCode) {
		this.underWriteCertiCode = underWriteCertiCode;
	}

	public double getUnderDefLoss() {
		return underDefLoss;
	}

	public void setUnderDefLoss(double underDefLoss) {
		this.underDefLoss = underDefLoss;
	}

	public String getIsTotalLoss() {
		return isTotalLoss;
	}

	public void setIsTotalLoss(String isTotalLoss) {
		this.isTotalLoss = isTotalLoss;
	}

	public String getIsHotSinceDetonation() {
		return isHotSinceDetonation;
	}

	public void setIsHotSinceDetonation(String isHotSinceDetonation) {
		this.isHotSinceDetonation = isHotSinceDetonation;
	}

	public String getIsWaterFlooded() {
		return isWaterFlooded;
	}

	public void setIsWaterFlooded(String isWaterFlooded) {
		this.isWaterFlooded = isWaterFlooded;
	}

	public String getWaterFloodedLevel() {
		return waterFloodedLevel;
	}

	public void setWaterFloodedLevel(String waterFloodedLevel) {
		this.waterFloodedLevel = waterFloodedLevel;
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
