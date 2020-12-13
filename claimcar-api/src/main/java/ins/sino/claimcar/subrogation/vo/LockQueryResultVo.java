/******************************************************************************
* CREATETIME : 2016年3月16日 下午2:53:04
******************************************************************************/
package ins.sino.claimcar.subrogation.vo;



import java.io.Serializable;



/**
 * 锁定查询 页面返回VO
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
public class LockQueryResultVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 保险公司 **/ 
	private String insurerCode; 

	/** 承保地区 **/ 
	private String insurerArea; 

	/** 保单险种类型 **/ 
	private String coverageType; 
	
	//责任对方保单号
	private String policyNo; 

	/** 商三限额 **/ 
	private Double limitAmount; 

	/** 是否承保商三不计免赔 **/ 
	private String insuredCA; 

	/** 号牌种类代码 **/ 
	private String licensePlateType; 

	/** 号牌号码 **/ 
	private String licensePlateNo; 

	/** 发动机号 **/ 
	private String engineNo; 

	/** VIN码 **/ 
	private String vin; 
	
	// 匹配次数
	private String matchTimes;

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

	public String getInsuredCA() {
		return insuredCA;
	}

	public void setInsuredCA(String insuredCA) {
		this.insuredCA = insuredCA;
	}

	public String getLicensePlateType() {
		return licensePlateType;
	}

	public void setLicensePlateType(String licensePlateType) {
		this.licensePlateType = licensePlateType;
	}

	public String getLicensePlateNo() {
		return licensePlateNo;
	}

	public void setLicensePlateNo(String licensePlateNo) {
		this.licensePlateNo = licensePlateNo;
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

	public String getMatchTimes() {
		return matchTimes;
	}

	public void setMatchTimes(String matchTimes) {
		this.matchTimes = matchTimes;
	} 
	
	
}
