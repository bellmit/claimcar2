/******************************************************************************
 * CREATETIME : 2016年6月1日 下午7:30:55
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * @author ★XMSH
 */
public class SHBIVClaimResBodyVo {

	@XmlElement(name = "DRIVER_NAME")
	private String driverName;// 出险驾驶员姓名

	@XmlElement(name = "CERTI_TYPE", required = true)
	private String certiType;// 出险驾驶员证件类型

	@XmlElement(name = "CERTI_CODE")
	private String certiCode;// 出险驾驶员证件号码

	@XmlElement(name = "LICENSE_NO")
	private String licenseNo;// 出险驾驶员档案编号

	@XmlElement(name = "LICENSE_EFFECTURAL_DATE")
	private String licenseEffecturalDate;// 出险驾驶员驾驶证有效日期

	@XmlElementWrapper(name = "VEHICLE_LIST")
	@XmlElement(name = "VEHICLE_DATA")
	private List<SHBIVClaimResVehicleDataVo> vehicleList;

	@XmlElementWrapper(name = "SUBROGATION_LIST")
	@XmlElement(name = "SUBROGATION_DATA")
	private List<SHBIVClaimResSubrogationDataVo> subrogationList;

	/**
	 * @return 返回 driverName 出险驾驶员姓名
	 */
	public String getDriverName() {
		return driverName;
	}

	/**
	 * @param driverName 要设置的 出险驾驶员姓名
	 */
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	/**
	 * @return 返回 certiType 出险驾驶员证件类型
	 */
	public String getCertiType() {
		return certiType;
	}

	/**
	 * @param certiType 要设置的 出险驾驶员证件类型
	 */
	public void setCertiType(String certiType) {
		this.certiType = certiType;
	}

	/**
	 * @return 返回 certiCode 出险驾驶员证件号码
	 */
	public String getCertiCode() {
		return certiCode;
	}

	/**
	 * @param certiCode 要设置的 出险驾驶员证件号码
	 */
	public void setCertiCode(String certiCode) {
		this.certiCode = certiCode;
	}

	/**
	 * @return 返回 licenseNo 出险驾驶员档案编号
	 */
	public String getLicenseNo() {
		return licenseNo;
	}

	/**
	 * @param licenseNo 要设置的 出险驾驶员档案编号
	 */
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	/**
	 * @return 返回 licenseEffecturalDate 出险驾驶员驾驶证有效日期
	 */
	public String getLicenseEffecturalDate() {
		return licenseEffecturalDate;
	}

	/**
	 * @param licenseEffecturalDate 要设置的 出险驾驶员驾驶证有效日期
	 */
	public void setLicenseEffecturalDate(String licenseEffecturalDate) {
		this.licenseEffecturalDate = licenseEffecturalDate;
	}

}
