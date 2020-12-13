/******************************************************************************
 * CREATETIME : 2016年5月30日 下午1:10:51
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 第三方车辆情况（多条）
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHBIClaimReqThirdVehicleDataVo {

	/**
	 * 第三方车辆号牌号码
	 */
	@XmlElement(name = "CAR_MARK")
	private String carMark;

	/**
	 * 第三方车辆号牌种类代码
	 */
	@XmlElement(name = "VEHICLE_TYPE")
	private String vehicleType;

	/**
	 * 第三方车辆车辆种类代码
	 */
	@XmlElement(name = "VEHICLE_CATEGORY")
	private String vehicleCategory;

	/**
	 * 第三方车辆承保公司代码
	 */
	@XmlElement(name = "COMPANY_CODE")
	private String companyCode;

	/**
	 * 第三方车辆保单号
	 */
	@XmlElement(name = "POLICY_NO")
	private String policyNo;

	/**
	 * 第三方车辆驾驶员姓名
	 */
	@XmlElement(name = "NAME")
	private String name;

	/**
	 * 第三方车辆驾驶员身份证明号码
	 */
	@XmlElement(name = "CERTI_CODE")
	private String certiCode;

	/**
	 * 第三方车辆驾驶员驾驶证号码
	 */
	@XmlElement(name = "LICENSE_NO")
	private String licenseNo;

	
	
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

	public String getVehicleCategory() {
		return vehicleCategory;
	}

	public void setVehicleCategory(String vehicleCategory) {
		this.vehicleCategory = vehicleCategory;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCertiCode() {
		return certiCode;
	}

	public void setCertiCode(String certiCode) {
		this.certiCode = certiCode;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

}
