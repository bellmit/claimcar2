/******************************************************************************
 * CREATETIME : 2016年5月31日 下午7:32:27
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 上海厘算核赔返回信息
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class SHCIVClaimResBodyVo {

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
	private List<SHCIVClaimResVehicleDataVo> vehicleList;

	@XmlElementWrapper(name = "SUBROGATION_LIST")
	@XmlElement(name = "SUBROGATION_DATA")
	private List<SHCIVClaimResSubrogationDataVo> subrogationList;

	/**
	 * @return 返回 driverName。
	 */
	public String getDriverName() {
		return driverName;
	}

	/**
	 * @param driverName 要设置的 driverName。
	 */
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	/**
	 * @return 返回 certiType。
	 */
	public String getCertiType() {
		return certiType;
	}

	/**
	 * @param certiType 要设置的 certiType。
	 */
	public void setCertiType(String certiType) {
		this.certiType = certiType;
	}

	/**
	 * @return 返回 certiCode。
	 */
	public String getCertiCode() {
		return certiCode;
	}

	/**
	 * @param certiCode 要设置的 certiCode。
	 */
	public void setCertiCode(String certiCode) {
		this.certiCode = certiCode;
	}

	/**
	 * @return 返回 licenseNo。
	 */
	public String getLicenseNo() {
		return licenseNo;
	}

	/**
	 * @param licenseNo 要设置的 licenseNo。
	 */
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	/**
	 * @return 返回 licenseEffecturalDate。
	 */
	public String getLicenseEffecturalDate() {
		return licenseEffecturalDate;
	}

	/**
	 * @param licenseEffecturalDate 要设置的 licenseEffecturalDate。
	 */
	public void setLicenseEffecturalDate(String licenseEffecturalDate) {
		this.licenseEffecturalDate = licenseEffecturalDate;
	}

	/**
	 * @return 返回 vehicleList。
	 */
	public List<SHCIVClaimResVehicleDataVo> getVehicleList() {
		return vehicleList;
	}

	/**
	 * @param vehicleList 要设置的 vehicleList。
	 */
	public void setVehicleList(List<SHCIVClaimResVehicleDataVo> vehicleList) {
		this.vehicleList = vehicleList;
	}

	/**
	 * @return 返回 subrogationList。
	 */
	public List<SHCIVClaimResSubrogationDataVo> getSubrogationList() {
		return subrogationList;
	}

	/**
	 * @param subrogationList 要设置的 subrogationList。
	 */
	public void setSubrogationList(List<SHCIVClaimResSubrogationDataVo> subrogationList) {
		this.subrogationList = subrogationList;
	}

}
