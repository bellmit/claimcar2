package ins.sino.claimcar.lossprop.vo;

import java.io.Serializable;

public class PropModifyVo implements Serializable{
	private static final long serialVersionUID = 1L; 
	private String mercyFlag;//案件紧急程度
	private String customerLevel;//客户登记
	private String registNo;//报案号
	private String policyNo;//保单号
	private String lossType;//损失方 0地面1标的3三者
	private String license;//车牌号
	/*项目损失项,损失方为0,取值为地面财产损失,1取值为标的车财产损失,3取值为三者车财产损失*/
	private String lossTypeName;
	private String insuredName;//被保险人
	private String cusTypeCode;//客户类型
	private String comCode;//承保机构
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
	public String getLossType() {
		return lossType;
	}
	public void setLossType(String lossType) {
		this.lossType = lossType;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getLossTypeName() {
		return lossTypeName;
	}
	public void setLossTypeName(String lossTypeName) {
		this.lossTypeName = lossTypeName;
	}
	public String getInsuredName() {
		return insuredName;
	}
	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}
	public String getCusTypeCode() {
		return cusTypeCode;
	}
	public void setCusTypeCode(String cusTypeCode) {
		this.cusTypeCode = cusTypeCode;
	}
	public String getComCode() {
		return comCode;
	}
	public void setComCode(String comCode) {
		this.comCode = comCode;
	}
	

}
