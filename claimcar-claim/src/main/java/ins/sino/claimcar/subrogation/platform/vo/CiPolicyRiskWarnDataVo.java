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
public class CiPolicyRiskWarnDataVo {
	
	/**保险公司 **/
	@XmlElement(name="INSURER_CODE")
	private String insurerCode;
	/**承保地区 **/
	@XmlElement(name="INSURER_AREA")
	private String insurerArea;
	/**保单险种类型 **/
	@XmlElement(name="COVERAGE_TYPE")
	private String coverageType;
	/**货币 商三限额 **/
	@XmlElement(name="LIMIT_AMOUNT")
	private Double limitAmount;
	/**节假日限额翻倍险限额 **/
	@XmlElement(name="FESTIVAL_AMOUNT")
	private Double festivalAmount;
	/**是否承保商三不计免赔**/
	@XmlElement(name="IS_INSURED_CA")
	private String insuredCA;
	/**号牌号码 **/
	@XmlElement(name="CAR_MARK")
	private String carMark;
	/**号牌种类 **/
	@XmlElement(name="VEHICLE_TYPE")
	private String licenseType;
	/**发动机号 **/
	@XmlElement(name="ENGINE_NO")
	private String engineNo;
	/**vin码 **/
	@XmlElement(name="RACK_NO")
	private String rackNo;
	
	public String getInsurerCode() {
		return insurerCode;
	}
	
	public void setInsurerCode(String insurerCode) {
		this.insurerCode = insurerCode;
	}
	
	public String getInsurerArea() {
		return insurerArea;
	}
	
	public void setInsurerArea(String insurerArea) {
		this.insurerArea = insurerArea;
	}
	
	public String getCoverageType() {
		return coverageType;
	}
	
	public void setCoverageType(String coverageType) {
		this.coverageType = coverageType;
	}
	
	public Double getLimitAmount() {
		return limitAmount;
	}
	
	public void setLimitAmount(Double limitAmount) {
		this.limitAmount = limitAmount;
	}
	
	
	public Double getFestivalAmount() {
		return festivalAmount;
	}

	public void setFestivalAmount(Double festivalAmount) {
		this.festivalAmount = festivalAmount;
	}

	public String getInsuredCA() {
		return insuredCA;
	}
	
	public void setInsuredCA(String insuredCA) {
		this.insuredCA = insuredCA;
	}
	
	public String getCarMark() {
		return carMark;
	}
	
	public void setCarMark(String carMark) {
		this.carMark = carMark;
	}
	
	public String getLicenseType() {
		return licenseType;
	}
	
	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
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
	
	
	
}
