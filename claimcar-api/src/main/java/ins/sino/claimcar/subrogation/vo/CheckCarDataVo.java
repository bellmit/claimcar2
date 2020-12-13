/******************************************************************************
* CREATETIME : 2016年3月17日 下午7:39:34
******************************************************************************/
package ins.sino.claimcar.subrogation.vo;

import java.io.Serializable;



//互审信息
/**
 * @author ★YangKun
 * @CreateTime 2016年3月17日
 */
public class CheckCarDataVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/** 损失车辆号牌号码 **/ 
	private String carMark; 

	/** 损失车辆号牌种类代码 **/ 
	private String vehicleType; 

	/** 损失车辆VIN码 **/ 
	private String rackNo; 

	/** 损失车辆发动机号 **/ 
	private String engineNo; 

	/** 损失车辆厂牌型号 **/ 
	private String vehicleModel; 

	/** 出险驾驶员姓名 **/ 
	private String driverName; 

	/** 车辆属性 **/ 
	private String vehicleProperty; 

	/** 现场类别；参见代码 **/ 
	private String fieldType; 

	/** 估损金额 **/ 
	private Double estimateAmount; 

	/** 查勘地点 **/ 
	private String checkAddr; 

	/** 查勘情况说明 **/ 
	private String checkDes;

	private String licensePlateNo;//损失出险车辆号牌号码

	private String licensePlateType;//损失出险车辆号牌种类代码；参见代码

	private String vIN;//损失出险车辆VIN码

	private String model;//损失出险车辆车辆型号

	private Double estimatedLossAmount;//估损金额

	private String checkDesc;//查勘情况说明
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

	public String getLicensePlateNo() {
		return licensePlateNo;
	}

	public void setLicensePlateNo(String licensePlateNo) {
		this.licensePlateNo = licensePlateNo;
	}

	public String getLicensePlateType() {
		return licensePlateType;
	}

	public void setLicensePlateType(String licensePlateType) {
		this.licensePlateType = licensePlateType;
	}

	public String getvIN() {
		return vIN;
	}

	public void setvIN(String vIN) {
		this.vIN = vIN;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Double getEstimatedLossAmount() {
		return estimatedLossAmount;
	}

	public void setEstimatedLossAmount(Double estimatedLossAmount) {
		this.estimatedLossAmount = estimatedLossAmount;
	}

	public String getCheckDesc() {
		return checkDesc;
	}

	public void setCheckDesc(String checkDesc) {
		this.checkDesc = checkDesc;
	}

	
}
