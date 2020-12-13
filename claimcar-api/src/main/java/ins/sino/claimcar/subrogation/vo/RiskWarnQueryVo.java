package ins.sino.claimcar.subrogation.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 风险预警承保信息查询页面VO
 * @author ★YangKun
 * @CreateTime 2016年3月18日
 */
public class RiskWarnQueryVo implements Serializable{

	/**  */
	private static final long serialVersionUID = 1L;

	private String registNo;
	
	private String licenseNo;
	
	private String licenseType;

	private String vinNo;
	
	private String areaCode;
	
	private String engineNo;
	
	private String riskCode;
	
	private String riskCodeSub;
	
	private String queryType;
	
	private String recoveryCode;
	
	private String policyNo;
	
	private Date damageDate;
	
	private String comCode;
	public String getRegistNo() {
		return registNo;
	}

	
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	
	public String getLicenseNo() {
		return licenseNo;
	}

	
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	
	public String getLicenseType() {
		return licenseType;
	}

	
	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}

	
	public String getVinNo() {
		return vinNo;
	}

	
	public void setVinNo(String vinNo) {
		this.vinNo = vinNo;
	}
	
	public String getEngineNo() {
		return engineNo;
	}

	
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	
	public String getRiskCode() {
		return riskCode;
	}

	
	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}

	
	public String getQueryType() {
		return queryType;
	}

	
	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}


	public String getRecoveryCode() {
		return recoveryCode;
	}


	public void setRecoveryCode(String recoveryCode) {
		this.recoveryCode = recoveryCode;
	}


	public String getPolicyNo() {
		return policyNo;
	}


	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}


	public Date getDamageDate() {
		return damageDate;
	}


	public void setDamageDate(Date damageDate) {
		this.damageDate = damageDate;
	}


	public String getComCode() {
		return comCode;
	}


	public void setComCode(String comCode) {
		this.comCode = comCode;
	}


	public String getAreaCode() {
		return areaCode;
	}


	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}


	public String getRiskCodeSub() {
		return riskCodeSub;
	}


	public void setRiskCodeSub(String riskCodeSub) {
		this.riskCodeSub = riskCodeSub;
	}


}
