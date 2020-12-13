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
public class LockedThirdPartyData {
	/** 责任对方三者车号牌号码 **/ 
	@XmlElement(name="LicensePlateNo")
	private String licensePlateNo; 

	/** 责任对方三者车号牌种类 **/ 
	@XmlElement(name="LicensePlateType")
	private String licensePlateType; 

	/** 责任对方三者车发动机号 **/ 
	@XmlElement(name="EngineNo")
	private String engineNo; 

	/** 责任对方三者车VIN码 **/ 
	@XmlElement(name="VIN")
	private String vin;

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






}
