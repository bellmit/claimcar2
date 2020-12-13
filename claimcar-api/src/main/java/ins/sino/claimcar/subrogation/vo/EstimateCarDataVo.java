/******************************************************************************
* CREATETIME : 2016年3月17日 下午7:39:34
******************************************************************************/
package ins.sino.claimcar.subrogation.vo;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

//车辆损失情况
/**
 * @author ★YangKun
 * @CreateTime 2016年3月17日
 */
public class EstimateCarDataVo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/** 损失车辆号牌号码 **/ 
	private String carMark; 

	/** 损失车辆号牌种类代码；参见代码 **/ 
	private String vehicleType; 

	/** 损失车辆发动机号 **/ 
	private String engineNo; 

	/** 损失车辆VIN码 **/ 
	private String rackNo; 

	/** 车辆型号 **/ 
	private String vehicleModel; 

	/** 出险驾驶员证件类型；参见代码 **/ 
	private String certiType; 

	/** 出险驾驶员证件号码 **/ 
	private String certiCode; 

	/** 核损金额 **/ 
	private Double underDefLoss; 

	/** 车辆属性(本车/三者车)；参见代码 **/ 
	private String vehicleProperty; 

	/** 是否盗抢；参见代码 **/ 
	private String isRobber; 

	/** 现场类别；参见代码 **/ 
	private String fieldType; 

	/** 残值回收预估金额 **/ 
	private Double remnant; 

	/** 是否修理或更换配件；参见代码 **/ 
	private String isChangeOrRepair; 

	/** 修理机构名称 **/ 
	private String repairFactoryName;
	
	/** 车辆损失部位列表（隶属于车辆损失情况）*/
	private List<EstimateCarLossPartDataVo> lossPartDataList;
	
	/** 车辆配件明细列表（隶属于车辆损失情况）*/
	private List<EstimateCarFittingDataVo> fittingDataList;

	@XmlElement(name = "LicensePlateNo")
	private String licensePlateNo;//损失出险车辆号牌号码

	@XmlElement(name = "LicensePlateType")
	private String licensePlateType;//损失出险车辆号牌种类代码；参见代码

	

	@XmlElement(name = "VIN")
	private String vIN;//损失出险车辆VIN码

	@XmlElement(name = "Model")
	private String model;//损失出险车辆车辆型号

	
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

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public String getRackNo() {
		return rackNo;
	}

	public void setRackNo(String rackNo) {
		this.rackNo = rackNo;
	}

	public String getVehicleModel() {
		return vehicleModel;
	}

	public void setVehicleModel(String vehicleModel) {
		this.vehicleModel = vehicleModel;
	}

	public String getCertiType() {
		return certiType;
	}

	public void setCertiType(String certiType) {
		this.certiType = certiType;
	}

	public String getCertiCode() {
		return certiCode;
	}

	public void setCertiCode(String certiCode) {
		this.certiCode = certiCode;
	}

	public Double getUnderDefLoss() {
		return underDefLoss;
	}

	public void setUnderDefLoss(Double underDefLoss) {
		this.underDefLoss = underDefLoss;
	}

	public String getVehicleProperty() {
		return vehicleProperty;
	}

	public void setVehicleProperty(String vehicleProperty) {
		this.vehicleProperty = vehicleProperty;
	}

	public String getIsRobber() {
		return isRobber;
	}

	public void setIsRobber(String isRobber) {
		this.isRobber = isRobber;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public Double getRemnant() {
		return remnant;
	}

	public void setRemnant(Double remnant) {
		this.remnant = remnant;
	}

	public String getIsChangeOrRepair() {
		return isChangeOrRepair;
	}

	public void setIsChangeOrRepair(String isChangeOrRepair) {
		this.isChangeOrRepair = isChangeOrRepair;
	}

	public String getRepairFactoryName() {
		return repairFactoryName;
	}

	public void setRepairFactoryName(String repairFactoryName) {
		this.repairFactoryName = repairFactoryName;
	}

	public List<EstimateCarLossPartDataVo> getLossPartDataList() {
		return lossPartDataList;
	}

	public void setLossPartDataList(List<EstimateCarLossPartDataVo> lossPartDataList) {
		this.lossPartDataList = lossPartDataList;
	}

	public List<EstimateCarFittingDataVo> getFittingDataList() {
		return fittingDataList;
	}

	public void setFittingDataList(List<EstimateCarFittingDataVo> fittingDataList) {
		this.fittingDataList = fittingDataList;
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
	
}
