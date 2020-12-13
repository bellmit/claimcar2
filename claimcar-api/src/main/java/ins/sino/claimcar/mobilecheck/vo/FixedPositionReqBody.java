package ins.sino.claimcar.mobilecheck.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 客户定位界面接口请求body（理赔请求快赔）
 * @author zy
 *
 */
@XStreamAlias("BODY")
public class FixedPositionReqBody implements Serializable {

	/**  */
	private static final long serialVersionUID = 1L;

	/** 省代码 */
	@XStreamAlias("PROVINCECODE")
	private String provinceCode;
	
	/** 城市代码 */
	@XStreamAlias("CITYCODE")
	private String cityCode;
	
	/** 区域代码 */
	@XStreamAlias("REGIONCODE")
	private String regionCode;
	
	/** 出险地点 */
	@XStreamAlias("DAMAGEADDRESS")
	private String damageAddress;
	
	/** 是否上海保单 */
	@XStreamAlias("ISSHPOLICY")
	private String isSHPolicy;

	
	
	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getDamageAddress() {
		return damageAddress;
	}

	public void setDamageAddress(String damageAddress) {
		this.damageAddress = damageAddress;
	}

	public String getIsSHPolicy() {
		return isSHPolicy;
	}

	public void setIsSHPolicy(String isSHPolicy) {
		this.isSHPolicy = isSHPolicy;
	}
	
	
	
	
}
