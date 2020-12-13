/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

//车辆查勘情况列表(隶属于查勘信息)
/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiCheckCarDataVo {
	
	/** 损失车辆号牌号码 **/ 
	@XmlElement(name="CAR_MARK")
	private String carMark; 

	/** 损失车辆号牌种类代码 **/ 
	@XmlElement(name="VEHICLE_TYPE")
	private String vehicleType; 

	/** 损失车辆VIN码 **/ 
	@XmlElement(name="RACK_NO")
	private String rackNo; 

	/** 损失车辆发动机号 **/ 
	@XmlElement(name="ENGINE_NO")
	private String engineNo; 

	/** 损失车辆厂牌型号 **/ 
	@XmlElement(name="VEHICLE_MODEL")
	private String vehicleModel; 

	/** 出险驾驶员姓名 **/ 
	@XmlElement(name="DRIVER_NAME")
	private String driverName; 

	/** 车辆属性 **/ 
	@XmlElement(name="VEHICLE_PROPERTY", required = true)
	private String vehicleProperty; 

	/** 现场类别；参见代码 **/ 
	@XmlElement(name="FIELD_TYPE", required = true)
	private String fieldType; 

	/** 估损金额 **/ 
	@XmlElement(name="ESTIMATE_AMOUNT")
	private Double estimateAmount; 

	/** 查勘地点 **/ 
	@XmlElement(name="CHECK_ADDR")
	private String checkAddr; 

	/** 查勘情况说明 **/ 
	@XmlElement(name="CHECK_DES")
	private String checkDes;

	public String getCarMark() {
		return carMark;
	}

	public void setCarMark(String carMark) {
		this.carMark = carMark;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getRackNo() {
		return rackNo;
	}

	public void setRackNo(String rackNo) {
		this.rackNo = rackNo;
	}

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public String getVehicleModel() {
		return vehicleModel;
	}

	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getVehicleProperty() {
		return vehicleProperty;
	}

	public void setVehicleProperty(String vehicleProperty) {
		this.vehicleProperty = vehicleProperty;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public Double getEstimateAmount() {
		return estimateAmount;
	}

	public void setEstimateAmount(Double estimateAmount) {
		this.estimateAmount = estimateAmount;
	}

	public String getCheckAddr() {
		return checkAddr;
	}

	public void setCheckAddr(String checkAddr) {
		this.checkAddr = checkAddr;
	}

	public String getCheckDes() {
		return checkDes;
	}

	public void setCheckDes(String checkDes) {
		this.checkDes = checkDes;
	} 



}
