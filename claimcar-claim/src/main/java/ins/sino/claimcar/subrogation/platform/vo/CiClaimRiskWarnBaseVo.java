/******************************************************************************
* CREATETIME : 2016年3月16日 下午12:10:37
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;


/**
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiClaimRiskWarnBaseVo {
	/** 报案号 **/ 
	@XmlElement(name="REPORT_NO", required = true)
	private String reportNo; 

	/** 号牌号码 **/ 
	@XmlElement(name="CAR_MARK")
	private String carMark; 

	/** 号牌种类代码 **/ 
	@XmlElement(name="VEHICLE_TYPE")
	private String vehicleType; 

	/** 发动机号 **/ 
	@XmlElement(name="ENGINE_NO")
	private String engineNo; 

	/** VIN码 **/ 
	@XmlElement(name="RACK_NO")
	private String rackNo; 

	/** 地区代码 **/ 
	@XmlElement(name="AREA_CODE")
	private String areaCode;

	public String getReportNo() {
		return reportNo;
	}

	public void setReportNo(String reportNo) {
		this.reportNo = reportNo;
	}

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

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	} 


	
}
