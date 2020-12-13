package ins.sino.claimcar.ciitc.push.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
@XStreamAlias("ResInformation") 
public class ResInformation implements Serializable{
	private static final long serialVersionUID = 1L;

	@XStreamAlias("licenseNo")
	private String licenseNo;//车辆号牌号码
	
	@XStreamAlias("licenseType")
	private String licenseType;//车辆号牌种类
	
	@XStreamAlias("engineNo")
	private String engineNo;//车辆发动机号
	
	@XStreamAlias("vin")
	private String vin;//车辆车架号

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
