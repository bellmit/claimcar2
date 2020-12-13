/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
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
public class CiSubrogationDataVo {
	/** 责任对方联系人姓名/名称 **/ 
	@XmlElement(name="LINKER_NAME")
	private String linkerName; 

	/** 责任对方车辆号牌号码 **/ 
	@XmlElement(name="CAR_MARK")
	private String carMark; 

	/** 责任对方车辆号牌种类 **/ 
	@XmlElement(name="VEHICLE_TYPE")
	private String vehicleType; 

	/** 责任对方车辆发动机号 **/ 
	@XmlElement(name="ENGINE_NO")
	private String engineNo; 

	/** 责任对方车辆VIN码 **/ 
	@XmlElement(name="RACK_NO")
	private String rackNo; 

	/** 责任对方商业三者险承保公司 **/ 
	@XmlElement(name="CA_INSURER_CODE")
	private String caInsurerCode; 

	/** 责任对方商业三者险承保地区（到省市） **/ 
	@XmlElement(name="CA_INSURER_AREA")
	private String caInsurerArea; 

	/** 责任对方交强险承保公司；参见代码 **/ 
	@XmlElement(name="IA_INSURER_CODE")
	private String iaInsurerCode; 

	/** 责任对方交强险承保地区（到省市） **/ 
	@XmlElement(name="IA_INSURER_AREA")
	private String iaInsurerArea;

	public String getLinkerName() {
		return linkerName;
	}

	public void setLinkerName(String linkerName) {
		this.linkerName = linkerName;
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

	public String getCaInsurerCode() {
		return caInsurerCode;
	}

	public void setCaInsurerCode(String caInsurerCode) {
		this.caInsurerCode = caInsurerCode;
	}

	public String getCaInsurerArea() {
		return caInsurerArea;
	}

	public void setCaInsurerArea(String caInsurerArea) {
		this.caInsurerArea = caInsurerArea;
	}

	public String getIaInsurerCode() {
		return iaInsurerCode;
	}

	public void setIaInsurerCode(String iaInsurerCode) {
		this.iaInsurerCode = iaInsurerCode;
	}

	public String getIaInsurerArea() {
		return iaInsurerArea;
	}

	public void setIaInsurerArea(String iaInsurerArea) {
		this.iaInsurerArea = iaInsurerArea;
	} 

}
