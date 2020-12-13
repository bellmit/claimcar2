package ins.sino.claimcar.regist.vo;

public class PrpLpolicyNoViewVoBase implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private String registNo;
	private String policyNo;
	private String policyNoLink;//关联保单号
	private String licenseNo;//标的车车牌号
	private String insuredname;//被保险人
	private String frameNo;//标的车车架号
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
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public String getInsuredname() {
		return insuredname;
	}
	public void setInsuredname(String insuredname) {
		this.insuredname = insuredname;
	}
	public String getFrameNo() {
		return frameNo;
	}
	public void setFrameNo(String frameNo) {
		this.frameNo = frameNo;
	}
	
	

}
