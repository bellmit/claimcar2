/******************************************************************************
 * CREATETIME : 2016年6月6日 下午7:27:35
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHBIEndCaseThirdVehicleDataVo {

	@XmlElement(name = "CAR_MARK")
	private String carMark;// 第三方车辆号牌号码

	@XmlElement(name = "VEHICLE_TYPE", required = true)
	private String vehicleType;// 第三方车辆号牌种类代码

	@XmlElement(name = "VEHICLE_CATEGORY", required = true)
	private String vehicleCategory;// 第三方车辆车辆种类代码

	@XmlElement(name = "COMPANY_CODE", required = true)
	private String companyCode;// 第三方车辆承保公司代码

	@XmlElement(name = "POLICY_NO")
	private String policyNo;// 第三方车辆保单号

	@XmlElement(name = "NAME")
	private String name;// 第三方车辆驾驶员姓名

	@XmlElement(name = "CERTI_CODE")
	private String certiCode;// 第三方车辆驾驶员身份证明号码

	@XmlElement(name = "LICENSE_NO")
	private String licenseNo;// 第三方车辆驾驶员驾驶证号码

	/**
	 * @return 返回 carMark 第三方车辆号牌号码
	 */
	public String getCarMark() {
		return carMark;
	}

	/**
	 * @param carMark 要设置的 第三方车辆号牌号码
	 */
	public void setCarMark(String carMark) {
		this.carMark = carMark;
	}

	/**
	 * @return 返回 vehicleType 第三方车辆号牌种类代码
	 */
	public String getVehicleType() {
		return vehicleType;
	}

	/**
	 * @param vehicleType 要设置的 第三方车辆号牌种类代码
	 */
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	/**
	 * @return 返回 vehicleCategory 第三方车辆车辆种类代码
	 */
	public String getVehicleCategory() {
		return vehicleCategory;
	}

	/**
	 * @param vehicleCategory 要设置的 第三方车辆车辆种类代码
	 */
	public void setVehicleCategory(String vehicleCategory) {
		this.vehicleCategory = vehicleCategory;
	}

	/**
	 * @return 返回 companyCode 第三方车辆承保公司代码
	 */
	public String getCompanyCode() {
		return companyCode;
	}

	/**
	 * @param companyCode 要设置的 第三方车辆承保公司代码
	 */
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	/**
	 * @return 返回 policyNo 第三方车辆保单号
	 */
	public String getPolicyNo() {
		return policyNo;
	}

	/**
	 * @param policyNo 要设置的 第三方车辆保单号
	 */
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	/**
	 * @return 返回 name 第三方车辆驾驶员姓名
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name 要设置的 第三方车辆驾驶员姓名
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return 返回 certiCode 第三方车辆驾驶员身份证明号码
	 */
	public String getCertiCode() {
		return certiCode;
	}

	/**
	 * @param certiCode 要设置的 第三方车辆驾驶员身份证明号码
	 */
	public void setCertiCode(String certiCode) {
		this.certiCode = certiCode;
	}

	/**
	 * @return 返回 licenseNo 第三方车辆驾驶员驾驶证号码
	 */
	public String getLicenseNo() {
		return licenseNo;
	}

	/**
	 * @param licenseNo 要设置的 第三方车辆驾驶员驾驶证号码
	 */
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

}
