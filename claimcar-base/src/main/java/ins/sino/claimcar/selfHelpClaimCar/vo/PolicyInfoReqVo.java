package ins.sino.claimcar.selfHelpClaimCar.vo;


import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("POLICYINFO")
public class PolicyInfoReqVo {

	@XStreamAlias("INSUREDNAME")
	private String insuredName;//被保险人姓名
	@XStreamAlias("LICENSENO")
	private String licenseNo;//车牌号
	@XStreamAlias("IDENTIFYTYPE")
	private String identifyType;//证件类型
	@XStreamAlias("IDENTIFYNUMBER")
	private String identifyNumber;//证件号码
	@XStreamAlias("ENGINENO")
	private String engineNo;//发动机
	@XStreamAlias("FRAMENO")
	private String frameNo;//车架号
	public String getInsuredName() {
		return insuredName;
	}
	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}
	public String getLicenseNo() {
		return licenseNo;
	}
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}
	public String getIdentifyType() {
		return identifyType;
	}
	public void setIdentifyType(String identifyType) {
		this.identifyType = identifyType;
	}
	public String getIdentifyNumber() {
		return identifyNumber;
	}
	public void setIdentifyNumber(String identifyNumber) {
		this.identifyNumber = identifyNumber;
	}
	public String getEngineNo() {
		return engineNo;
	}
	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}
	public String getFrameNo() {
		return frameNo;
	}
	public void setFrameNo(String frameNo) {
		this.frameNo = frameNo;
	}
	
	
}
