/******************************************************************************
* CREATETIME : 2016年3月17日 下午7:39:34
******************************************************************************/
package ins.sino.claimcar.subrogation.vo;

import java.io.Serializable;



//代位信息列表
/**
 * @author ★YangKun
 * @CreateTime 2016年3月17日
 */
public class SubrogationDataVos implements Serializable{
	private static final long serialVersionUID = 1L;
	
	/** 责任对方联系人姓名/名称 **/ 
	private String linkerName; 

	/** 责任对方车辆号牌号码 **/ 
	private String carMark; 

	/** 责任对方车辆号牌种类 **/ 
	private String vehicleType; 

	/** 责任对方车辆发动机号 **/ 
	private String engineNo; 

	/** 责任对方车辆VIN码 **/ 
	private String rackNo; 

	/** 责任对方商业三者险承保公司 **/ 
	private String caInsurerCode; 

	/** 责任对方商业三者险承保地区（到省市） **/ 
	private String caInsurerArea; 

	/** 责任对方交强险承保公司；参见代码 **/ 
	private String iaInsurerCode; 

	/** 责任对方交强险承保地区（到省市） **/ 
	private String iaInsurerArea;

	private String licensePlateNo;//责任对方车辆号牌号码

	private String licensePlateType;//责任对方车辆号牌种类；参见代码

	private String vIN;//责任对方车辆发动机号

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
	
}
