/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.vo;



import java.io.Serializable;



/**
 * 风险预警承保信息查询 页面返回VO
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
public class PolicyRiskWarnVo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	/**保险公司 **/
	private String insurerCode;
	/**承保地区 **/
	private String insurerArea;
	/**保单险种类型 **/
	private String coverageType;
	/**货币 商三限额 **/
	private Double limitAmount;
	/**节假日限额翻倍险限额 **/
	private Double festivalAmount;
	/**是否承保商三不计免赔**/
	private String insuredCA;
	/**号牌号码 **/
	private String carMark;
	/**号牌种类 **/
	private String licenseType;
	/**发动机号 **/
	private String engineNo;
	/**vin码 **/
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
