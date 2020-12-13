/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

//车辆损失情况列表(隶属于定核损信息)
/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BiEstimateCarDataVo {
	@XmlElement(name = "LicensePlateNo")
	private String licensePlateNo;//损失出险车辆号牌号码

	@XmlElement(name = "LicensePlateType")
	private String licensePlateType;//损失出险车辆号牌种类代码；参见代码

	@XmlElement(name = "EngineNo")
	private String engineNo;//损失出险车辆发动机号

	@XmlElement(name = "VIN")
	private String vIN;//损失出险车辆VIN码

	@XmlElement(name = "Model")
	private String model;//损失出险车辆车辆型号

	@XmlElement(name = "CertiType")
	private String certiType;//出险驾驶员证件类型；参见代码

	@XmlElement(name = "CertiCode")
	private String certiCode;//出险驾驶员证件号码

	@XmlElement(name = "UnderDefLoss", required = true)
	private Double underDefLoss;//核损金额

	@XmlElement(name = "VehicleProperty", required = true)
	private String vehicleProperty;//车辆属性；参见代码

	@XmlElement(name = "IsRobber", required = true)
	private String isRobber;//是否盗抢

	@XmlElement(name = "FieldType", required = true)
	private String fieldType;//现场类别；参见代码

	@XmlElement(name = "Remnant")
	private Double remnant;//残值回收预估金额

	@XmlElement(name = "IsChangeOrRepair")
	private String isChangeOrRepair;//是否修理或更换配件；参见代码

	@XmlElement(name = "RepairFactoryName")
	private String repairFactoryName;//修理机构名称


	/** 
	 * @return 返回 licensePlateNo  损失出险车辆号牌号码
	 */ 
	public String getLicensePlateNo(){ 
	    return licensePlateNo;
	}

	/** 
	 * @param licensePlateNo 要设置的 损失出险车辆号牌号码
	 */ 
	public void setLicensePlateNo(String licensePlateNo){ 
	    this.licensePlateNo=licensePlateNo;
	}

	/** 
	 * @return 返回 licensePlateType  损失出险车辆号牌种类代码；参见代码
	 */ 
	public String getLicensePlateType(){ 
	    return licensePlateType;
	}

	/** 
	 * @param licensePlateType 要设置的 损失出险车辆号牌种类代码；参见代码
	 */ 
	public void setLicensePlateType(String licensePlateType){ 
	    this.licensePlateType=licensePlateType;
	}

	/** 
	 * @return 返回 engineNo  损失出险车辆发动机号
	 */ 
	public String getEngineNo(){ 
	    return engineNo;
	}

	/** 
	 * @param engineNo 要设置的 损失出险车辆发动机号
	 */ 
	public void setEngineNo(String engineNo){ 
	    this.engineNo=engineNo;
	}

	/** 
	 * @return 返回 vIN  损失出险车辆VIN码
	 */ 
	public String getVIN(){ 
	    return vIN;
	}

	/** 
	 * @param vIN 要设置的 损失出险车辆VIN码
	 */ 
	public void setVIN(String vIN){ 
	    this.vIN=vIN;
	}

	/** 
	 * @return 返回 model  损失出险车辆车辆型号
	 */ 
	public String getModel(){ 
	    return model;
	}

	/** 
	 * @param model 要设置的 损失出险车辆车辆型号
	 */ 
	public void setModel(String model){ 
	    this.model=model;
	}

	/** 
	 * @return 返回 certiType  出险驾驶员证件类型；参见代码
	 */ 
	public String getCertiType(){ 
	    return certiType;
	}

	/** 
	 * @param certiType 要设置的 出险驾驶员证件类型；参见代码
	 */ 
	public void setCertiType(String certiType){ 
	    this.certiType=certiType;
	}

	/** 
	 * @return 返回 certiCode  出险驾驶员证件号码
	 */ 
	public String getCertiCode(){ 
	    return certiCode;
	}

	/** 
	 * @param certiCode 要设置的 出险驾驶员证件号码
	 */ 
	public void setCertiCode(String certiCode){ 
	    this.certiCode=certiCode;
	}

	/** 
	 * @return 返回 underDefLoss  核损金额
	 */ 
	public Double getUnderDefLoss(){ 
	    return underDefLoss;
	}

	/** 
	 * @param underDefLoss 要设置的 核损金额
	 */ 
	public void setUnderDefLoss(Double underDefLoss){ 
	    this.underDefLoss=underDefLoss;
	}

	/** 
	 * @return 返回 vehicleProperty  车辆属性；参见代码
	 */ 
	public String getVehicleProperty(){ 
	    return vehicleProperty;
	}

	/** 
	 * @param vehicleProperty 要设置的 车辆属性；参见代码
	 */ 
	public void setVehicleProperty(String vehicleProperty){ 
	    this.vehicleProperty=vehicleProperty;
	}

	/** 
	 * @return 返回 isRobber  是否盗抢
	 */ 
	public String getIsRobber(){ 
	    return isRobber;
	}

	/** 
	 * @param isRobber 要设置的 是否盗抢
	 */ 
	public void setIsRobber(String isRobber){ 
	    this.isRobber=isRobber;
	}

	/** 
	 * @return 返回 fieldType  现场类别；参见代码
	 */ 
	public String getFieldType(){ 
	    return fieldType;
	}

	/** 
	 * @param fieldType 要设置的 现场类别；参见代码
	 */ 
	public void setFieldType(String fieldType){ 
	    this.fieldType=fieldType;
	}

	/** 
	 * @return 返回 remnant  残值回收预估金额
	 */ 
	public Double getRemnant(){ 
	    return remnant;
	}

	/** 
	 * @param remnant 要设置的 残值回收预估金额
	 */ 
	public void setRemnant(Double remnant){ 
	    this.remnant=remnant;
	}

	/** 
	 * @return 返回 isChangeOrRepair  是否修理或更换配件；参见代码
	 */ 
	public String getIsChangeOrRepair(){ 
	    return isChangeOrRepair;
	}

	/** 
	 * @param isChangeOrRepair 要设置的 是否修理或更换配件；参见代码
	 */ 
	public void setIsChangeOrRepair(String isChangeOrRepair){ 
	    this.isChangeOrRepair=isChangeOrRepair;
	}

	/** 
	 * @return 返回 repairFactoryName  修理机构名称
	 */ 
	public String getRepairFactoryName(){ 
	    return repairFactoryName;
	}

	/** 
	 * @param repairFactoryName 要设置的 修理机构名称
	 */ 
	public void setRepairFactoryName(String repairFactoryName){ 
	    this.repairFactoryName=repairFactoryName;
	}

	
	/** 车辆损失部位列表（隶属于车辆损失情况）*/
	@XmlElement(name = "LossPartData")
	private List<BiEstimateCarLossPartDataVo> lossPartDataList;
	
	/** 车辆配件明细列表（隶属于车辆损失情况）*/
	@XmlElement(name = "FittingData")
	private List<BiEstimateCarFittingDataVo> fittingDataList;


	public String getvIN() {
		return vIN;
	}

	public void setvIN(String vIN) {
		this.vIN = vIN;
	}

	public List<BiEstimateCarLossPartDataVo> getLossPartDataList() {
		return lossPartDataList;
	}

	public void setLossPartDataList(
			List<BiEstimateCarLossPartDataVo> lossPartDataList) {
		this.lossPartDataList = lossPartDataList;
	}

	public List<BiEstimateCarFittingDataVo> getFittingDataList() {
		return fittingDataList;
	}

	public void setFittingDataList(List<BiEstimateCarFittingDataVo> fittingDataList) {
		this.fittingDataList = fittingDataList;
	}

	
	
}
