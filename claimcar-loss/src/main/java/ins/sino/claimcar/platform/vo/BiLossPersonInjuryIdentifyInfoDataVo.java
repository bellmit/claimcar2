package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class BiLossPersonInjuryIdentifyInfoDataVo {

	@XmlElement(name = "InjuryIdentifyName", required = true)
	private String injuryIdentifyName;// 伤残鉴定机构名称

	@XmlElement(name = "InjuryIdentifyCertiCode")
	private String injuryIdentifyCertiCode;// 伤残鉴定机构组织机构代码

	/**
	 * @return 返回 injuryIdentifyName 伤残鉴定机构名称
	 */
	public String getInjuryIdentifyName() {
		return injuryIdentifyName;
	}

	/**
	 * @param injuryIdentifyName 要设置的 伤残鉴定机构名称
	 */
	public void setInjuryIdentifyName(String injuryIdentifyName) {
		this.injuryIdentifyName = injuryIdentifyName;
	}

	/**
	 * @return 返回 injuryIdentifyCertiCode 伤残鉴定机构组织机构代码
	 */
	public String getInjuryIdentifyCertiCode() {
		return injuryIdentifyCertiCode;
	}

	/**
	 * @param injuryIdentifyCertiCode 要设置的 伤残鉴定机构组织机构代码
	 */
	public void setInjuryIdentifyCertiCode(String injuryIdentifyCertiCode) {
		this.injuryIdentifyCertiCode = injuryIdentifyCertiCode;
	}

}
