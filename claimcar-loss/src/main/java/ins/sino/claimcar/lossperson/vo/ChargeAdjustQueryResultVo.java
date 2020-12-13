package ins.sino.claimcar.lossperson.vo;

import java.io.Serializable;

/**
 * 人伤费用审核修改查询结果VO
 * @author ★XMSH
 */
public class ChargeAdjustQueryResultVo  implements Serializable {
	private static final long serialVersionUID = 1L; 
	
	private Long persTraceMainId;// 定损任务号
	private String licenseNo;// 车牌号
	private String mercyFlag;//案件紧急程度
	private String customerLevel;//客户等级
	private String registNo;//报案号
	private String policyNo;//保单号
	private String policyNoLink;
	private String insuredCode;
	private String insuredName;//被保险人
	private String remark;//备注
	
	public Long getPersTraceMainId() {
		return persTraceMainId;
	}

	
	public void setPersTraceMainId(Long persTraceMainId) {
		this.persTraceMainId = persTraceMainId;
	}

	public String getLicenseNo() {
		return licenseNo;
	}
	
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	
	public String getMercyFlag() {
		return mercyFlag;
	}
	
	public void setMercyFlag(String mercyFlag) {
		this.mercyFlag = mercyFlag;
	}
	
	public String getCustomerLevel() {
		return customerLevel;
	}
	
	public void setCustomerLevel(String customerLevel) {
		this.customerLevel = customerLevel;
	}
	
	public String getRegistNo() {
		return registNo;
	}
	
	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}
	
	public String getPolicyNo() {
		return policyNo;
	}
	
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	
	
	public String getPolicyNoLink() {
		return policyNoLink;
	}


	
	public void setPolicyNoLink(String policyNoLink) {
		this.policyNoLink = policyNoLink;
	}


	public String getInsuredCode() {
		return insuredCode;
	}
	
	public void setInsuredCode(String insuredCode) {
		this.insuredCode = insuredCode;
	}
	
	public String getInsuredName() {
		return insuredName;
	}
	
	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}
	
	public String getRemark() {
		return remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
