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
public class BiCheckCarDataVo {
	
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

	@XmlElement(name = "DriverName")
	private String driverName;//出险车辆驾驶员姓名

	@XmlElement(name = "VehicleProperty", required = true)
	private String vehicleProperty;//车辆属性；参见代码

	@XmlElement(name = "FieldType", required = true)
	private String fieldType;//现场类别；参见代码

	@XmlElement(name = "EstimatedLossAmount")
	private Double estimatedLossAmount;//估损金额

	@XmlElement(name = "CheckAddr")
	private String checkAddr;//查勘地点

	@XmlElement(name = "CheckDesc")
	private String checkDesc;//查勘情况说明


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
	
	public String getvIN() {
		return vIN;
	}
	/** 
	 * @param vIN 要设置的 损失出险车辆VIN码
	 */ 
	public void setvIN(String vIN) {
		this.vIN = vIN;
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
	 * @return 返回 driverName  出险车辆驾驶员姓名
	 */ 
	public String getDriverName(){ 
	    return driverName;
	}

	/** 
	 * @param driverName 要设置的 出险车辆驾驶员姓名
	 */ 
	public void setDriverName(String driverName){ 
	    this.driverName=driverName;
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
	 * @return 返回 estimatedLossAmount  估损金额
	 */ 
	public Double getEstimatedLossAmount(){ 
	    return estimatedLossAmount;
	}

	/** 
	 * @param estimatedLossAmount 要设置的 估损金额
	 */ 
	public void setEstimatedLossAmount(Double estimatedLossAmount){ 
	    this.estimatedLossAmount=estimatedLossAmount;
	}

	/** 
	 * @return 返回 checkAddr  查勘地点
	 */ 
	public String getCheckAddr(){ 
	    return checkAddr;
	}

	/** 
	 * @param checkAddr 要设置的 查勘地点
	 */ 
	public void setCheckAddr(String checkAddr){ 
	    this.checkAddr=checkAddr;
	}

	/** 
	 * @return 返回 checkDesc  查勘情况说明
	 */ 
	public String getCheckDesc(){ 
	    return checkDesc;
	}

	/** 
	 * @param checkDesc 要设置的 查勘情况说明
	 */ 
	public void setCheckDesc(String checkDesc){ 
	    this.checkDesc=checkDesc;
	}




}
