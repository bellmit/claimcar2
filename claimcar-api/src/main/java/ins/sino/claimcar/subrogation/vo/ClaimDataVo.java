/******************************************************************************
* CREATETIME : 2016年3月17日 下午7:39:34
******************************************************************************/
package ins.sino.claimcar.subrogation.vo;


//报案查询
/**
 * @author ★YangKun
 * @CreateTime 2016年3月17日
 */
public class ClaimDataVo  implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String insurerCode;//对方保险公司；参见代码

	private String insurerArea;//对方承保地区；参见代码

	private String coverageType;//对方保单险种类型；参见代码

	private String policyNo;//对方保单号

	private Double limitAmount;//商三限额

	private String isInsuredCA;//是否承保商三不计免赔；参见代码

	private String licensePlateNo;//对方本车号牌号码

	private String licensePlateType;//对方本车号牌种类

	private String engineNo;//对方本车发动机号

	private String vin;//对方本车VIN码

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

	public String getPolicyNo() {
		return policyNo;
	}

	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}

	public Double getLimitAmount() {
		return limitAmount;
	}

	public void setLimitAmount(Double limitAmount) {
		this.limitAmount = limitAmount;
	}

	public String getIsInsuredCA() {
		return isInsuredCA;
	}

	public void setIsInsuredCA(String isInsuredCA) {
		this.isInsuredCA = isInsuredCA;
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

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}


	
	
	
}
