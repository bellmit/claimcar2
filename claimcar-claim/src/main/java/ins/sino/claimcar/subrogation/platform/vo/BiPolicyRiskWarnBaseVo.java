/******************************************************************************
* CREATETIME : 2016年3月17日 下午7:19:11
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
public class BiPolicyRiskWarnBaseVo {

	/** 报案号 **/ 
	@XmlElement(name="ClaimNotificationNo", required = true)
	private String claimNotificationNo; 

	/** 号牌种类代码 **/ 
	@XmlElement(name="LicensePlateType")
	private String licensePlateType; 

	/** 号牌号码 **/ 
	@XmlElement(name="LicensePlateNo")
	private String licensePlateNo; 

	/** 发动机号 **/ 
	@XmlElement(name="EngineNo")
	private String engineNo; 

	/** VIN码 **/ 
	@XmlElement(name="VIN")
	private String vin; 

	/** 地区代码 **/ 
	@XmlElement(name="AreaCode")
	private String areaCode;

	
	public String getClaimNotificationNo() {
		return claimNotificationNo;
	}

	
	public void setClaimNotificationNo(String claimNotificationNo) {
		this.claimNotificationNo = claimNotificationNo;
	}

	
	public String getLicensePlateType() {
		return licensePlateType;
	}

	
	public void setLicensePlateType(String licensePlateType) {
		this.licensePlateType = licensePlateType;
	}

	
	public String getLicensePlateNo() {
		return licensePlateNo;
	}

	
	public void setLicensePlateNo(String licensePlateNo) {
		this.licensePlateNo = licensePlateNo;
	}

	
	public String getEngineNo() {
		return engineNo;
	}

	
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	
	public String getVin() {
		return vin;
	}

	
	public void setVin(String vin) {
		this.vin = vin;
	}

	
	public String getAreaCode() {
		return areaCode;
	}

	
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	} 


	
}
