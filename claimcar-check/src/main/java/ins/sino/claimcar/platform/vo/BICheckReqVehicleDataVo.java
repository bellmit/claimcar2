/******************************************************************************
 * CREATETIME : 2016年4月27日 上午11:40:41
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 车险信息平台交强险V6.0.0接口 -->查勘登记vo -->商业--> 车辆损失情况列表
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BICheckReqVehicleDataVo {

	@XmlElement(name = "LicensePlateNo")
	private String licensePlateNo;// 出险车辆号牌号码

	@XmlElement(name = "LicensePlateType")
	private String licensePlateType;// 出险车辆号牌种类代码；参见代码

	@XmlElement(name = "VIN")
	private String vIN;// 出险车辆VIN码

	@XmlElement(name = "EngineNo")
	private String engineNo;// 出险车辆发动机号

	@XmlElement(name = "Model")
	private String model;// 出险车辆车辆型号

	@XmlElement(name = "DriverName")
	private String driverName;// 出险车辆驾驶员姓名

	@XmlElement(name = "DriverLicenseNo")
	private String driverLicenseNo;// 出险车辆驾驶证号码

	@XmlElement(name = "VehicleProperty", required = true)
	private String vehicleProperty;// 车辆属性；参见代码

	@XmlElement(name = "FieldType", required = true)
	private String fieldType;// 现场类别；参见代码

	@XmlElement(name = "EstimatedLossAmount")
	private BigDecimal estimatedLossAmount;// 估损金额

	@XmlElement(name = "CheckerName", required = true)
	private String checkerName;// 查勘人员姓名

	@XmlElement(name = "CheckerCode")
	private String checkerCode;// 查勘人员代码

	@XmlElement(name = "CheckerCertiCode", required = true)
	private String checkerCertiCode;// 查勘人员身份证号码

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "CheckStartTime", required = true)
	private Date checkStartTime;// 车辆损失查勘调度开始时间；精确到分钟

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "CheckEndTime", required = true)
	private Date checkEndTime;// 车辆损失查勘结束时间；精确到分钟

	@XmlElement(name = "CheckAddr")
	private String checkAddr;// 查勘地点

	@XmlElement(name = "CheckDesc")
	private String checkDesc;// 查勘情况说明
	
	@XmlElement(name = "MotorTypeCode")
	private String MotorTypeCode;// 车辆种类

	public String getMotorTypeCode() {
		return MotorTypeCode;
	}

	public void setMotorTypeCode(String motorTypeCode) {
		MotorTypeCode = motorTypeCode;
	}

	/**
	 * @return 返回 licensePlateNo 出险车辆号牌号码
	 */
	public String getLicensePlateNo() {
		return licensePlateNo;
	}

	/**
	 * @param licensePlateNo 要设置的 出险车辆号牌号码
	 */
	public void setLicensePlateNo(String licensePlateNo) {
		this.licensePlateNo = licensePlateNo;
	}

	/**
	 * @return 返回 licensePlateType 出险车辆号牌种类代码；参见代码
	 */
	public String getLicensePlateType() {
		return licensePlateType;
	}

	/**
	 * @param licensePlateType 要设置的 出险车辆号牌种类代码；参见代码
	 */
	public void setLicensePlateType(String licensePlateType) {
		this.licensePlateType = licensePlateType;
	}

	/**
	 * @return 返回 vIN 出险车辆VIN码
	 */
	public String getVIN() {
		return vIN;
	}

	/**
	 * @param vIN 要设置的 出险车辆VIN码
	 */
	public void setVIN(String vIN) {
		this.vIN = vIN;
	}

	/**
	 * @return 返回 engineNo 出险车辆发动机号
	 */
	public String getEngineNo() {
		return engineNo;
	}

	/**
	 * @param engineNo 要设置的 出险车辆发动机号
	 */
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	/**
	 * @return 返回 model 出险车辆车辆型号
	 */
	public String getModel() {
		return model;
	}

	/**
	 * @param model 要设置的 出险车辆车辆型号
	 */
	public void setModel(String model) {
		this.model = model;
	}

	/**
	 * @return 返回 driverName 出险车辆驾驶员姓名
	 */
	public String getDriverName() {
		return driverName;
	}

	/**
	 * @param driverName 要设置的 出险车辆驾驶员姓名
	 */
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	/**
	 * @return 返回 driverLicenseNo 出险车辆驾驶证号码
	 */
	public String getDriverLicenseNo() {
		return driverLicenseNo;
	}

	/**
	 * @param driverLicenseNo 要设置的 出险车辆驾驶证号码
	 */
	public void setDriverLicenseNo(String driverLicenseNo) {
		this.driverLicenseNo = driverLicenseNo;
	}

	/**
	 * @return 返回 vehicleProperty 车辆属性；参见代码
	 */
	public String getVehicleProperty() {
		return vehicleProperty;
	}

	/**
	 * @param vehicleProperty 要设置的 车辆属性；参见代码
	 */
	public void setVehicleProperty(String vehicleProperty) {
		this.vehicleProperty = vehicleProperty;
	}

	/**
	 * @return 返回 fieldType 现场类别；参见代码
	 */
	public String getFieldType() {
		return fieldType;
	}

	/**
	 * @param fieldType 要设置的 现场类别；参见代码
	 */
	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	/**
	 * @return 返回 estimatedLossAmount 估损金额
	 */
	public BigDecimal getEstimatedLossAmount() {
		return estimatedLossAmount;
	}

	/**
	 * @param estimatedLossAmount 要设置的 估损金额
	 */
	public void setEstimatedLossAmount(BigDecimal estimatedLossAmount) {
		this.estimatedLossAmount = estimatedLossAmount;
	}

	/**
	 * @return 返回 checkerName 查勘人员姓名
	 */
	public String getCheckerName() {
		return checkerName;
	}

	/**
	 * @param checkerName 要设置的 查勘人员姓名
	 */
	public void setCheckerName(String checkerName) {
		this.checkerName = checkerName;
	}

	/**
	 * @return 返回 checkerCode 查勘人员代码
	 */
	public String getCheckerCode() {
		return checkerCode;
	}

	/**
	 * @param checkerCode 要设置的 查勘人员代码
	 */
	public void setCheckerCode(String checkerCode) {
		this.checkerCode = checkerCode;
	}

	/**
	 * @return 返回 checkerCertiCode 查勘人员身份证号码
	 */
	public String getCheckerCertiCode() {
		return checkerCertiCode;
	}

	/**
	 * @param checkerCertiCode 要设置的 查勘人员身份证号码
	 */
	public void setCheckerCertiCode(String checkerCertiCode) {
		this.checkerCertiCode = checkerCertiCode;
	}

	/**
	 * @return 返回 checkStartTime 车辆损失查勘调度开始时间；精确到分钟
	 */
	public Date getCheckStartTime() {
		return checkStartTime;
	}

	/**
	 * @param checkStartTime 要设置的 车辆损失查勘调度开始时间；精确到分钟
	 */
	public void setCheckStartTime(Date checkStartTime) {
		this.checkStartTime = checkStartTime;
	}

	/**
	 * @return 返回 checkEndTime 车辆损失查勘结束时间；精确到分钟
	 */
	public Date getCheckEndTime() {
		return checkEndTime;
	}

	/**
	 * @param checkEndTime 要设置的 车辆损失查勘结束时间；精确到分钟
	 */
	public void setCheckEndTime(Date checkEndTime) {
		this.checkEndTime = checkEndTime;
	}

	/**
	 * @return 返回 checkAddr 查勘地点
	 */
	public String getCheckAddr() {
		return checkAddr;
	}

	/**
	 * @param checkAddr 要设置的 查勘地点
	 */
	public void setCheckAddr(String checkAddr) {
		this.checkAddr = checkAddr;
	}

	/**
	 * @return 返回 checkDesc 查勘情况说明
	 */
	public String getCheckDesc() {
		return checkDesc;
	}

	/**
	 * @param checkDesc 要设置的 查勘情况说明
	 */
	public void setCheckDesc(String checkDesc) {
		this.checkDesc = checkDesc;
	}
}
