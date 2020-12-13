package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 住院详细信息
 * <pre></pre>
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiLossPersonHospitalInfoDataVo {

	@XmlElement(name = "HOSPITAL_NAME")
	private String hospitalName;// 治疗机构名称

	@XmlElement(name = "HOSPITAL_FACTORY_CERTI_CODE")
	private String hospitalFactoryCertiCode;// 治疗机构组织机构代码

	/**
	 * @return 返回 hospitalName 治疗机构名称
	 */
	public String getHospitalName() {
		return hospitalName;
	}

	/**
	 * @param hospitalName 要设置的 治疗机构名称
	 */
	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	/**
	 * @return 返回 hospitalFactoryCertiCode 治疗机构组织机构代码
	 */
	public String getHospitalFactoryCertiCode() {
		return hospitalFactoryCertiCode;
	}

	/**
	 * @param hospitalFactoryCertiCode 要设置的 治疗机构组织机构代码
	 */
	public void setHospitalFactoryCertiCode(String hospitalFactoryCertiCode) {
		this.hospitalFactoryCertiCode = hospitalFactoryCertiCode;
	}

}
