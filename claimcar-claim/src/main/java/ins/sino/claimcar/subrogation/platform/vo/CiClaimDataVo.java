/******************************************************************************
* CREATETIME : 2016年3月17日 下午7:39:34
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


/**
 * @author ★YangKun
 * @CreateTime 2016年3月17日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiClaimDataVo {
	@XmlElement(name = "INSURER_CODE", required = true)
	private String insurerCode;//对方保险公司；参见代码

	@XmlElement(name = "INSURER_AREA", required = true)
	private String insurerArea;//对方承保地区；参见代码

	@XmlElement(name = "COVERAGE_TYPE", required = true)
	private String coverageType;//对方保单险种类型；参见代码

	@XmlElement(name = "POLICY_NO", required = true)
	private String policyNo;//对方保单号

	@XmlElement(name = "LIMIT_AMOUNT")
	private Double limitAmount;//商三限额

	@XmlElement(name = "IS_INSURED_CA")
	private String isInsuredCa;//是否承保商三不计免赔；参见代码

	@XmlElement(name = "CAR_MARK")
	private String carMark;//对方本车号牌号码

	@XmlElement(name = "VEHICLE_TYPE")
	private String vehicleType;//对方本车号牌种类；参见代码

	@XmlElement(name = "ENGINE_NO")
	private String engineNo;//对方本车发动机号

	@XmlElement(name = "RACK_NO", required = true)
	private String rackNo;//对方本车VIN码


	/** 
	 * @return 返回 insurerCode  对方保险公司；参见代码
	 */ 
	public String getInsurerCode(){ 
	    return insurerCode;
	}

	/** 
	 * @param insurerCode 要设置的 对方保险公司；参见代码
	 */ 
	public void setInsurerCode(String insurerCode){ 
	    this.insurerCode=insurerCode;
	}

	/** 
	 * @return 返回 insurerArea  对方承保地区；参见代码
	 */ 
	public String getInsurerArea(){ 
	    return insurerArea;
	}

	/** 
	 * @param insurerArea 要设置的 对方承保地区；参见代码
	 */ 
	public void setInsurerArea(String insurerArea){ 
	    this.insurerArea=insurerArea;
	}

	/** 
	 * @return 返回 coverageType  对方保单险种类型；参见代码
	 */ 
	public String getCoverageType(){ 
	    return coverageType;
	}

	/** 
	 * @param coverageType 要设置的 对方保单险种类型；参见代码
	 */ 
	public void setCoverageType(String coverageType){ 
	    this.coverageType=coverageType;
	}

	/** 
	 * @return 返回 policyNo  对方保单号
	 */ 
	public String getPolicyNo(){ 
	    return policyNo;
	}

	/** 
	 * @param policyNo 要设置的 对方保单号
	 */ 
	public void setPolicyNo(String policyNo){ 
	    this.policyNo=policyNo;
	}

	/** 
	 * @return 返回 limitAmount  商三限额
	 */ 
	public Double getLimitAmount(){ 
	    return limitAmount;
	}

	/** 
	 * @param limitAmount 要设置的 商三限额
	 */ 
	public void setLimitAmount(Double limitAmount){ 
	    this.limitAmount=limitAmount;
	}

	/** 
	 * @return 返回 isInsuredCa  是否承保商三不计免赔；参见代码
	 */ 
	public String getIsInsuredCa(){ 
	    return isInsuredCa;
	}

	/** 
	 * @param isInsuredCa 要设置的 是否承保商三不计免赔；参见代码
	 */ 
	public void setIsInsuredCa(String isInsuredCa){ 
	    this.isInsuredCa=isInsuredCa;
	}

	/** 
	 * @return 返回 carMark  对方本车号牌号码
	 */ 
	public String getCarMark(){ 
	    return carMark;
	}

	/** 
	 * @param carMark 要设置的 对方本车号牌号码
	 */ 
	public void setCarMark(String carMark){ 
	    this.carMark=carMark;
	}

	/** 
	 * @return 返回 vehicleType  对方本车号牌种类；参见代码
	 */ 
	public String getVehicleType(){ 
	    return vehicleType;
	}

	/** 
	 * @param vehicleType 要设置的 对方本车号牌种类；参见代码
	 */ 
	public void setVehicleType(String vehicleType){ 
	    this.vehicleType=vehicleType;
	}

	/** 
	 * @return 返回 engineNo  对方本车发动机号
	 */ 
	public String getEngineNo(){ 
	    return engineNo;
	}

	/** 
	 * @param engineNo 要设置的 对方本车发动机号
	 */ 
	public void setEngineNo(String engineNo){ 
	    this.engineNo=engineNo;
	}

	/** 
	 * @return 返回 rackNo  对方本车VIN码
	 */ 
	public String getRackNo(){ 
	    return rackNo;
	}

	/** 
	 * @param rackNo 要设置的 对方本车VIN码
	 */ 
	public void setRackNo(String rackNo){ 
	    this.rackNo=rackNo;
	}



}
