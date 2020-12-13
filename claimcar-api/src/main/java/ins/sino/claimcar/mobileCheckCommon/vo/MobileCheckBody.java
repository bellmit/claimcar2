package ins.sino.claimcar.mobileCheckCommon.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("BODY")
public class MobileCheckBody implements Serializable{
	/**  */
	private static final long serialVersionUID = -4623864062836013585L;
	
	@XStreamAlias("REGISTNO")
	private String registNo; //报案号

	@XStreamAlias("IFOBJECT")
	private String ifObject; //类别
	
	@XStreamAlias("LICENSENO")
	private String licenseNo; //车牌号
	
	@XStreamAlias("CLAIMTYPE")
	private String claimType; //理赔环节
	/*自助理赔*/
	@XStreamAlias("SYSTEMTYPE")
	private String systemType; //请求子系统
	
	public String getRegistNo() {
		return registNo;
	}

	public void setRegistNo(String registNo) {
		this.registNo = registNo;
	}

	public String getIfObject() {
		return ifObject;
	}

	public void setIfObject(String ifObject) {
		this.ifObject = ifObject;
	}

	public String getLicenseNo() {
		return licenseNo;
	}

	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	public String getClaimType() {
		return claimType;
	}

	public void setClaimType(String claimType) {
		this.claimType = claimType;
	}

	public String getSystemType() {
		return systemType;
	}

	public void setSystemType(String systemType) {
		this.systemType = systemType;
	}

	
	
}
