/******************************************************************************
 * CREATETIME : 2016年5月24日 下午5:19:30
 ******************************************************************************/
package ins.sino.claimcar.regist.platform.vo.sh;

import ins.sino.claimcar.carplatform.adapter.DateXmlAdapter;
import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 报案交强送上海平台返回-基本信息
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCIRegistResBasePartVo {

	@XmlElement(name = "CLAIM_CODE", required = true)
	private String claimCode;// 理赔编码

	@XmlElement(name = "DRIVER_NAME")
	private String driverName;// 出险驾驶员姓名

	@XmlElement(name = "CERTI_TYPE")
	private String certiType;// 出险驾驶员证件类型

	@XmlElement(name = "CERTI_CODE")
	private String certiCode;// 出险驾驶员证件号码

	@XmlElement(name = "LICENSE_NO")
	private String licenseNo;// 出险驾驶员档案编号

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "LICENSE_EFFECTURAL_DATE")
	private Date licenseEffecturalDate;// 出险驾驶员驾驶证有效日期

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "START_DATE", required = true)
	private Date startDate;// 出险保单起期

	@XmlJavaTypeAdapter(DateXmlAdapter.class)
	@XmlElement(name = "END_DATE", required = true)
	private Date endDate;// 出险保单止期

	
	
	/**
	 * @return 返回 claimCode 理赔编码
	 */
	public String getClaimCode() {
		return claimCode;
	}

	/**
	 * @param claimCode 要设置的 理赔编码
	 */
	public void setClaimCode(String claimCode) {
		this.claimCode = claimCode;
	}

	/**
	 * @return 返回 driverName 出险驾驶员姓名
	 */
	public String getDriverName() {
		return driverName;
	}

	/**
	 * @param driverName 要设置的 出险驾驶员姓名
	 */
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	/**
	 * @return 返回 certiType 出险驾驶员证件类型
	 */
	public String getCertiType() {
		return certiType;
	}

	/**
	 * @param certiType 要设置的 出险驾驶员证件类型
	 */
	public void setCertiType(String certiType) {
		this.certiType = certiType;
	}

	/**
	 * @return 返回 certiCode 出险驾驶员证件号码
	 */
	public String getCertiCode() {
		return certiCode;
	}

	/**
	 * @param certiCode 要设置的 出险驾驶员证件号码
	 */
	public void setCertiCode(String certiCode) {
		this.certiCode = certiCode;
	}

	/**
	 * @return 返回 licenseNo 出险驾驶员档案编号
	 */
	public String getLicenseNo() {
		return licenseNo;
	}

	/**
	 * @param licenseNo 要设置的 出险驾驶员档案编号
	 */
	public void setLicenseNo(String licenseNo) {
		this.licenseNo = licenseNo;
	}

	/**
	 * @return 返回 licenseEffecturalDate 出险驾驶员驾驶证有效日期
	 */
	public Date getLicenseEffecturalDate() {
		return licenseEffecturalDate;
	}

	/**
	 * @param licenseEffecturalDate 要设置的 出险驾驶员驾驶证有效日期
	 */
	public void setLicenseEffecturalDate(Date licenseEffecturalDate) {
		this.licenseEffecturalDate = licenseEffecturalDate;
	}

	/**
	 * @return 返回 startDate 出险保单起期
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate 要设置的 出险保单起期
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return 返回 endDate 出险保单止期
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate 要设置的 出险保单止期
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
