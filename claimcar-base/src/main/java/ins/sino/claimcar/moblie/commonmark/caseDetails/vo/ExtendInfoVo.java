package ins.sino.claimcar.moblie.commonmark.caseDetails.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * 报案基本信息（快赔请求理赔）
 * <pre></pre>
 * @author ★LinYi
 */
@XStreamAlias("EXTENDINFO")
public class ExtendInfoVo implements Serializable{
	
	/**  */
	private static final long serialVersionUID = 1L;
	
	@XStreamAlias("LICENSEEFFECTIVE")
	private String licenseEffective; //保险车辆行驶证是否有效

    
    public String getLicenseEffective() {
        return licenseEffective;
    }

    
    public void setLicenseEffective(String licenseEffective) {
        this.licenseEffective = licenseEffective;
    }
	
}
