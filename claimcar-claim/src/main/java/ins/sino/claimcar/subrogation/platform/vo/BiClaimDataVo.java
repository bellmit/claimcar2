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
public class BiClaimDataVo {
	@XmlElement(name = "InsurerCode", required = true)
	private String insurerCode;//对方保险公司；参见代码

	@XmlElement(name = "InsurerArea", required = true)
	private String insurerArea;//对方承保地区；参见代码

	@XmlElement(name = "CoverageType", required = true)
	private String coverageType;//对方保单险种类型；参见代码

	@XmlElement(name = "PolicyNo", required = true)
	private String policyNo;//对方保单号

	@XmlElement(name = "LimitAmount")
	private Double limitAmount;//商三限额

	@XmlElement(name = "IsInsuredCA")
	private String isInsuredCA;//是否承保商三不计免赔；参见代码

	@XmlElement(name = "LicensePlateNo")
	private String licensePlateNo;//对方本车号牌号码

	@XmlElement(name = "LicensePlateType")
	private String licensePlateType;//对方本车号牌种类

	@XmlElement(name = "EngineNo")
	private String engineNo;//对方本车发动机号

	@XmlElement(name = "VIN", required = true)
	private String vin;//对方本车VIN码


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
	 * @return 返回 isInsuredCA  是否承保商三不计免赔；参见代码
	 */ 
	public String getIsInsuredCA(){ 
	    return isInsuredCA;
	}

	/** 
	 * @param isInsuredCA 要设置的 是否承保商三不计免赔；参见代码
	 */ 
	public void setIsInsuredCA(String isInsuredCA){ 
	    this.isInsuredCA=isInsuredCA;
	}

	/** 
	 * @return 返回 licensePlateNo  对方本车号牌号码
	 */ 
	public String getLicensePlateNo(){ 
	    return licensePlateNo;
	}

	/** 
	 * @param licensePlateNo 要设置的 对方本车号牌号码
	 */ 
	public void setLicensePlateNo(String licensePlateNo){ 
	    this.licensePlateNo=licensePlateNo;
	}

	/** 
	 * @return 返回 licensePlateType  对方本车号牌种类
	 */ 
	public String getLicensePlateType(){ 
	    return licensePlateType;
	}

	/** 
	 * @param licensePlateType 要设置的 对方本车号牌种类
	 */ 
	public void setLicensePlateType(String licensePlateType){ 
	    this.licensePlateType=licensePlateType;
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
	 * @return 返回 vin  对方本车VIN码
	 */ 
	public String getVin() {
		return vin;
	}
	/** 
	 * @param vin 要设置的 对方本车VIN码
	 */ 
	public void setVin(String vin) {
		this.vin = vin;
	}

	
	

	







}
