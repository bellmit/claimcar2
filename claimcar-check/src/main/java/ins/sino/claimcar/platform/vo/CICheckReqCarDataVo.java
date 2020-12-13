/******************************************************************************
 * CREATETIME : 2016年4月13日 下午5:50:30
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;

import java.math.BigDecimal;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *车险信息平台交强险V6.0.0接口 --> 查勘登记vo --> 交强-->车辆查勘情况列表vo
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "CAR_DATA")
public class CICheckReqCarDataVo {

	@XmlElement(name = "CAR_MARK")
	private String carMark;// 损失出险车辆号牌号码

	@XmlElement(name = "VEHICLE_TYPE")
	private String vehicleType;// 损失出险车辆号牌种类代码；参见代码

	@XmlElement(name = "RACK_NO")
	private String rackNo;// 损失出险车辆VIN码

	@XmlElement(name = "ENGINE_NO")
	private String engineNo;// 损失出险车辆发动机号

	@XmlElement(name = "VEHICLE_MODEL")
	private String vehicleModel;// 损失出险车辆厂牌车辆型号

	@XmlElement(name = "DRIVER_NAME")
	private String driverName;// 出险车辆驾驶员姓名

	@XmlElement(name = "DRIVER_LICENSE_NO")
	private String driverLicenseNo;// 出险车辆驾驶证号码

	@XmlElement(name = "VEHICLE_PROPERTY", required = true)
	private String vehicleProperty;// 车辆属性；参见代码

	@XmlElement(name = "FIELD_TYPE", required = true)
	private String fieldType;// 现场类别；参见代码

	@XmlElement(name = "CHECKER_NAME")
	private String checkerName;// 查勘人员姓名

	@XmlElement(name = "CHECKER_CODE")
	private String checkerCode;// 查勘人员代码

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "CHECK_START_TIME", required = true)
	private Date checkStartTime;// 车辆损失查勘调度开始时间 格式：YYYYMMDDHHMM

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "CHECK_END_TIME", required = true)
	private Date checkEndTime;// 车辆损失查勘结束时间 格式：YYYYMMDDHHMM

	@XmlElement(name = "CHECK_ADDR")
	private String checkAddr;// 查勘地点

	@XmlElement(name = "CHECK_DES")
	private String checkDes;// 查勘情况说明

	@XmlElement(name = "ESTIMATE_AMOUNT")
	private BigDecimal estimateAmount;// 估损金额

	@XmlElement(name = "CHECKER_CERTI_CODE")
	private String checkerCertiCode;// 查勘人员身份证号码
	
	@XmlElement(name = "CARKIND")
	private String carKind;// 车辆种类
	
	public String getCarKind() {
		return carKind;
	}

	public void setCarKind(String carKind) {
		this.carKind = carKind;
	}

	/**
	 * @return 返回 carMark 损失出险车辆号牌号码
	 */
	public String getCarMark() {
		return carMark;
	}

	/**
	 * @param carMark 要设置的 损失出险车辆号牌号码
	 */
	public void setCarMark(String carMark) {
		this.carMark = carMark;
	}

	/**
	 * @return 返回 vehicleType 损失出险车辆号牌种类代码；参见代码
	 */
	public String getVehicleType() {
		return vehicleType;
	}

	/**
	 * @param vehicleType 要设置的 损失出险车辆号牌种类代码；参见代码
	 */
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	/**
	 * @return 返回 rackNo 损失出险车辆VIN码
	 */
	public String getRackNo() {
		return rackNo;
	}

	/**
	 * @param rackNo 要设置的 损失出险车辆VIN码
	 */
	public void setRackNo(String rackNo) {
		this.rackNo = rackNo;
	}

	/**
	 * @return 返回 engineNo 损失出险车辆发动机号
	 */
	public String getEngineNo() {
		return engineNo;
	}

	/**
	 * @param engineNo 要设置的 损失出险车辆发动机号
	 */
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	/**
	 * @return 返回 vehicleModel 损失出险车辆厂牌车辆型号
	 */
	public String getVehicleModel() {
		return vehicleModel;
	}

	/**
	 * @param vehicleModel 要设置的 损失出险车辆厂牌车辆型号
	 */
	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
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
	 * @return 返回 checkStartTime 车辆损失查勘调度开始时间 格式：YYYYMMDDHHMM
	 */
	public Date getCheckStartTime() {
		return checkStartTime;
	}

	/**
	 * @param checkStartTime 要设置的 车辆损失查勘调度开始时间 格式：YYYYMMDDHHMM
	 */
	public void setCheckStartTime(Date checkStartTime) {
		this.checkStartTime = checkStartTime;
	}

	/**
	 * @return 返回 checkEndTime 车辆损失查勘结束时间 格式：YYYYMMDDHHMM
	 */
	public Date getCheckEndTime() {
		return checkEndTime;
	}

	/**
	 * @param checkEndTime 要设置的 车辆损失查勘结束时间 格式：YYYYMMDDHHMM
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
	 * @return 返回 checkDes 查勘情况说明
	 */
	public String getCheckDes() {
		return checkDes;
	}

	/**
	 * @param checkDes 要设置的 查勘情况说明
	 */
	public void setCheckDes(String checkDes) {
		this.checkDes = checkDes;
	}

	/**
	 * @return 返回 estimateAmount 估损金额
	 */
	public BigDecimal getEstimateAmount() {
		return estimateAmount;
	}

	/**
	 * @param estimateAmount 要设置的 估损金额
	 */
	public void setEstimateAmount(BigDecimal estimateAmount) {
		this.estimateAmount = estimateAmount;
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
}
