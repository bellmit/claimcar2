/******************************************************************************
 * CREATETIME : 2016年5月23日 下午4:27:37
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class BiCertifySubrogationDataVo {

	@XmlElement(name = "LinkerName", required = true)
	private String linkerName;// 责任对方姓名/名称

	@XmlElement(name = "LicensePlateNo")
	private String licensePlateNo;// 责任对方车辆号牌号码

	@XmlElement(name = "LicensePlateType")
	private String licensePlateType;// 责任对方车辆号牌种类；参见代码

	@XmlElement(name = "VIN")
	private String vIN;// 责任对方车辆VIN码

	@XmlElement(name = "EngineNo")
	private String engineNo;// 责任对方车辆发动机号

	@XmlElement(name = "CaInsurerCode")
	private String caInsurerCode;// 责任对方商业三者险承保公司；参见代码

	@XmlElement(name = "CaInsurerArea")
	private String caInsurerArea;// 责任对方商业三者险承保地区（到省市）；参见代码

	@XmlElement(name = "IaInsurerCode")
	private String iaInsurerCode;// 责任对方交强险承保公司；参见代码

	@XmlElement(name = "IaInsurerArea")
	private String iaInsurerArea;// 责任对方交强险承保地区（到省市）；参见代码

	/**
	 * @return 返回 linkerName 责任对方姓名/名称
	 */
	public String getLinkerName() {
		return linkerName;
	}

	/**
	 * @param linkerName 要设置的 责任对方姓名/名称
	 */
	public void setLinkerName(String linkerName) {
		this.linkerName = linkerName;
	}

	/**
	 * @return 返回 licensePlateNo 责任对方车辆号牌号码
	 */
	public String getLicensePlateNo() {
		return licensePlateNo;
	}

	/**
	 * @param licensePlateNo 要设置的 责任对方车辆号牌号码
	 */
	public void setLicensePlateNo(String licensePlateNo) {
		this.licensePlateNo = licensePlateNo;
	}

	/**
	 * @return 返回 licensePlateType 责任对方车辆号牌种类；参见代码
	 */
	public String getLicensePlateType() {
		return licensePlateType;
	}

	/**
	 * @param licensePlateType 要设置的 责任对方车辆号牌种类；参见代码
	 */
	public void setLicensePlateType(String licensePlateType) {
		this.licensePlateType = licensePlateType;
	}

	/**
	 * @return 返回 vIN 责任对方车辆VIN码
	 */
	public String getVIN() {
		return vIN;
	}

	/**
	 * @param vIN 要设置的 责任对方车辆VIN码
	 */
	public void setVIN(String vIN) {
		this.vIN = vIN;
	}

	/**
	 * @return 返回 engineNo 责任对方车辆发动机号
	 */
	public String getEngineNo() {
		return engineNo;
	}

	/**
	 * @param engineNo 要设置的 责任对方车辆发动机号
	 */
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	/**
	 * @return 返回 caInsurerCode 责任对方商业三者险承保公司；参见代码
	 */
	public String getCaInsurerCode() {
		return caInsurerCode;
	}

	/**
	 * @param caInsurerCode 要设置的 责任对方商业三者险承保公司；参见代码
	 */
	public void setCaInsurerCode(String caInsurerCode) {
		this.caInsurerCode = caInsurerCode;
	}

	/**
	 * @return 返回 caInsurerArea 责任对方商业三者险承保地区（到省市）；参见代码
	 */
	public String getCaInsurerArea() {
		return caInsurerArea;
	}

	/**
	 * @param caInsurerArea 要设置的 责任对方商业三者险承保地区（到省市）；参见代码
	 */
	public void setCaInsurerArea(String caInsurerArea) {
		this.caInsurerArea = caInsurerArea;
	}

	/**
	 * @return 返回 iaInsurerCode 责任对方交强险承保公司；参见代码
	 */
	public String getIaInsurerCode() {
		return iaInsurerCode;
	}

	/**
	 * @param iaInsurerCode 要设置的 责任对方交强险承保公司；参见代码
	 */
	public void setIaInsurerCode(String iaInsurerCode) {
		this.iaInsurerCode = iaInsurerCode;
	}

	/**
	 * @return 返回 iaInsurerArea 责任对方交强险承保地区（到省市）；参见代码
	 */
	public String getIaInsurerArea() {
		return iaInsurerArea;
	}

	/**
	 * @param iaInsurerArea 要设置的 责任对方交强险承保地区（到省市）；参见代码
	 */
	public void setIaInsurerArea(String iaInsurerArea) {
		this.iaInsurerArea = iaInsurerArea;
	}

}
