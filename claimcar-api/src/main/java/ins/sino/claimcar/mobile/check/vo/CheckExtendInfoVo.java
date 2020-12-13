package ins.sino.claimcar.mobile.check.vo;

import java.io.Serializable;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("EXTENDINFO")
public class CheckExtendInfoVo implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("LICENSEEFFECTIVE")
	private String licenseEffective = "9"; //保险车辆行驶证是否有效

	public String getLicenseEffective() {
		return licenseEffective;
	}

	public void setLicenseEffective(String licenseEffective) {
		this.licenseEffective = licenseEffective;
	}
	
}
