package ins.sino.claimcar.moblie.caselist.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class MCqryCaseListReqBody implements Serializable {

	/**  */
	private static final long serialVersionUID = 6606450331852430142L;
	
	@XStreamAlias("HANDLERCODE")
	private String handlerCode;  //用户编码
	
	@XStreamAlias("HANDLERNAME")
	private String handlerName;  //用户名称
	
	@XStreamAlias("SYSTEMTYPE")
	private String systemType;  //请求子系统
	
	@XStreamAlias("LICENSENO")
	private String licenseNo;  //车牌号
	
	@XStreamAlias("REGISTNO")
	private String registNo;  // 报案号
	
	@XStreamAlias("INUREDNAME")
	private String insuredName;  //被保险人名称

	@XStreamAlias("IDENTIFYNUMBER")
	private String identifyNumber;  //被保险人证件号码
	
	public String getHandlerCode() {
		return handlerCode;
	}

	public void setHandlerCode(String handlerCode) {
		this.handlerCode = handlerCode;
	}

	public String getHandlerName() {
		return handlerName;
	}

	public void setHandlerName(String handlerName) {
		this.handlerName = handlerName;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getInsuredName() {
		return insuredName;
	}

	public void setInsuredName(String insuredName) {
		this.insuredName = insuredName;
	}

	public String getSystemType() {
		return systemType;
	}

	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}

	public String getIdentifyNumber() {
		return identifyNumber;
	}

	public void setIdentifyNumber(String identifyNumber) {
		this.identifyNumber = identifyNumber;
	}

	
}
