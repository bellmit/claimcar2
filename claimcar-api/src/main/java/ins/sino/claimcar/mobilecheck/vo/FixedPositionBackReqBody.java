package ins.sino.claimcar.mobilecheck.vo;

import java.io.Serializable;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 客户定位界面返回数据接口请求body（快赔请求理赔）
 * @author zy
 *
 */
@XStreamAlias("BODY")
public class FixedPositionBackReqBody implements Serializable {

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
	
	/** 坐标 */
	@XStreamAlias("LNGXLATY")
	private String lngXlatY;
	
	/** 是否上海保单 */
	@XStreamAlias("ISSHPOLICY")
	private String isSHPolicy;
	
	/** 自定义区域编码 */
	@XStreamAlias("SELFDEFINAREACODE")
	private String selfDefinareaCode;
	
	

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

	public String getLngXlatY() {
		return lngXlatY;
	}

	public void setLngXlatY(String lngXlatY) {
		this.lngXlatY = lngXlatY;
	}

	public String getIsSHPolicy() {
		return isSHPolicy;
	}

	public void setIsSHPolicy(String isSHPolicy) {
		this.isSHPolicy = isSHPolicy;
	}

	public String getSelfDefinareaCode() {
		return selfDefinareaCode;
	}

	public void setSelfDefinareaCode(String selfDefinareaCode) {
		this.selfDefinareaCode = selfDefinareaCode;
	}
	
	
}
