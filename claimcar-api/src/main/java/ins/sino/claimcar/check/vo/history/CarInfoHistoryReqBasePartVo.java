package ins.sino.claimcar.check.vo.history;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CarInfoHistoryReqBasePartVo implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@XmlElement(name = "ClaimNotificationNo", required = true)
	private String claimNotificationNo;// 报案号

	@XmlElement(name = "LicensePlateType")
	private String licensePlateType;// 号牌种类代码；参见代码

	@XmlElement(name = "LicensePlateNo")
	private String licensePlateNo;// 号牌号码

	@XmlElement(name = "EngineNo")
	private String engineNo;// 发动机号

	@XmlElement(name = "VIN")
	private String vin;// VIN码

	@XmlElement(name = "AreaCode")
	private String areaCode;// 地区代码;参加代码

	/**
	 * @return the claimNotificationNo
	 */
	public String getClaimNotificationNo() {
		return claimNotificationNo;
	}

	/**
	 * @param claimNotificationNo
	 *            the claimNotificationNo to set
	 */
	public void setClaimNotificationNo(String claimNotificationNo) {
		this.claimNotificationNo = claimNotificationNo;
	}

	/**
	 * @return the licensePlateType
	 */
	public String getLicensePlateType() {
		return licensePlateType;
	}

	/**
	 * @param licensePlateType
	 *            the licensePlateType to set
	 */
	public void setLicensePlateType(String licensePlateType) {
		this.licensePlateType = licensePlateType;
	}

	/**
	 * @return the licensePlateNo
	 */
	public String getLicensePlateNo() {
		return licensePlateNo;
	}

	/**
	 * @param licensePlateNo
	 *            the licensePlateNo to set
	 */
	public void setLicensePlateNo(String licensePlateNo) {
		this.licensePlateNo = licensePlateNo;
	}

	/**
	 * @return the engineNo
	 */
	public String getEngineNo() {
		return engineNo;
	}

	/**
	 * @param engineNo
	 *            the engineNo to set
	 */
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	/**
	 * @return the vin
	 */
	public String getVin() {
		return vin;
	}

	/**
	 * @param vin
	 *            the vin to set
	 */
	public void setVin(String vin) {
		this.vin = vin;
	}

	/**
	 * @return the areaCode
	 */
	public String getAreaCode() {
		return areaCode;
	}

	/**
	 * @param areaCode
	 *            the areaCode to set
	 */
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
}
