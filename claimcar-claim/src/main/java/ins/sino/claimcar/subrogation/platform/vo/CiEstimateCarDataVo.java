/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

//车辆损失情况列表(隶属于定核损信息)
/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiEstimateCarDataVo {
	/** 损失车辆号牌号码 **/ 
	@XmlElement(name="CAR_MARK")
	private String carMark; 

	/** 损失车辆号牌种类代码；参见代码 **/ 
	@XmlElement(name="VEHICLE_TYPE")
	private String vehicleType; 

	/** 损失车辆发动机号 **/ 
	@XmlElement(name="ENGINE_NO")
	private String engineNo; 

	/** 损失车辆VIN码 **/ 
	@XmlElement(name="RACK_NO")
	private String rackNo; 

	/** 车辆型号 **/ 
	@XmlElement(name="VEHICLE_MODEL")
	private String vehicleModel; 

	/** 出险驾驶员证件类型；参见代码 **/ 
	@XmlElement(name="CERTI_TYPE")
	private String certiType; 

	/** 出险驾驶员证件号码 **/ 
	@XmlElement(name="CERTI_CODE")
	private String certiCode; 

	/** 核损金额 **/ 
	@XmlElement(name="UNDER_DEF_LOSS", required = true)
	private Double underDefLoss; 

	/** 车辆属性(本车/三者车)；参见代码 **/ 
	@XmlElement(name="VEHICLE_PROPERTY", required = true)
	private String vehicleProperty; 

	/** 是否盗抢；参见代码 **/ 
	@XmlElement(name="IS_ROBBER", required = true)
	private String isRobber; 

	/** 现场类别；参见代码 **/ 
	@XmlElement(name="FIELD_TYPE", required = true)
	private String fieldType; 

	/** 残值回收预估金额 **/ 
	@XmlElement(name="REMNANT")
	private Double remnant; 

	/** 是否修理或更换配件；参见代码 **/ 
	@XmlElement(name="IS_CHANGE_OR_REPAIR")
	private String isChangeOrRepair; 

	/** 修理机构名称 **/ 
	@XmlElement(name="REPAIR_FACTORY_NAME")
	private String repairFactoryName;
	
	/** 车辆损失部位列表（隶属于车辆损失情况）*/
	@XmlElementWrapper(name = "LOSS_PART_LIST")
	@XmlElement(name = "LOSS_PART_DATA")
	private List<CiEstimateCarLossPartDataVo> lossPartDataList;
	
	/** 车辆配件明细列表（隶属于车辆损失情况）*/
	@XmlElementWrapper(name = "FITTING_LIST")
	@XmlElement(name = "FITTING_DATA")
	private List<CiEstimateCarFittingDataVo> fittingDataList;

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

	
	public List<CiEstimateCarLossPartDataVo> getLossPartDataList() {
		return lossPartDataList;
	}

	
	public void setLossPartDataList(List<CiEstimateCarLossPartDataVo> lossPartDataList) {
		this.lossPartDataList = lossPartDataList;
	}

	
	public List<CiEstimateCarFittingDataVo> getFittingDataList() {
		return fittingDataList;
	}

	
	public void setFittingDataList(List<CiEstimateCarFittingDataVo> fittingDataList) {
		this.fittingDataList = fittingDataList;
	} 


	 
	
}
