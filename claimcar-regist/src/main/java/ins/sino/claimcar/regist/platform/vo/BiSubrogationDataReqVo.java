package ins.sino.claimcar.regist.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 商业报案上传平台代位信息列表返回信息
 * @author dengkk
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BiSubrogationDataReqVo {
	
	/**
	 * 责任对方姓名/名称
	 */
	@XmlElement(name="LinkerName")
	private String linkerName;
	
	
	/**
	 * 责任对方车辆号牌号码
	 */
	@XmlElement(name="LicensePlateNo")
	private String licensePlateNo;
	
	
	/**
	 * 责任对方车辆号牌种类；参见代码
	 */
	@XmlElement(name="LicensePlateType")
	private String licensePlateType;
	
	/**
	 * 责任对方车辆VIN码
	 */
	@XmlElement(name="VIN")
	private String VIN;
	
	/**
	 * 责任对方车辆发动机号
	 */
	@XmlElement(name="EngineNo")
	private String engineNo;
	
	/**
	 * 责任对方商业三者险承保公司；参见代码
	 */
	@XmlElement(name="CaInsurerCode")
	private String caInsurerCode;
	
	/**
	 * 责任对方商业三者险承保地区（到省市）；参见代码
	 */
	@XmlElement(name="CaInsurerArea")
	private String caInsurerArea;
	
	/**
	 * 责任对方交强险承保公司；参见代码
	 */
	@XmlElement(name="IaInsurerCode")
	private String iaInsurerCode;
	
	/**
	 * 责任对方交强险承保地区（到省市）；参见代码
	 */
	@XmlElement(name="IaInsurerArea")
	private String iaInsurerArea;

	public String getLinkerName() {
		return linkerName;
	}

	public void setLinkerName(String linkerName) {
		this.linkerName = linkerName;
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

	public String getVIN() {
		return VIN;
	}

	public void setVIN(String vIN) {
		VIN = vIN;
	}

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
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
