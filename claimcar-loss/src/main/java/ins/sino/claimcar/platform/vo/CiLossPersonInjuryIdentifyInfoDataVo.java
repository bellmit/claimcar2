package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CiLossPersonInjuryIdentifyInfoDataVo {

	@XmlElement(name = "INJURY_IDENTIFY_NAME")
	private String injuryIdentifyName;// 伤残鉴定机构名称

	@XmlElement(name = "INJURY_IDENTIFY_CERTICODE")
	private String injuryIdentifyCerticode;// 伤残鉴定机构组织机构代码

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
	 * @return 返回 injuryIdentifyCerticode 伤残鉴定机构组织机构代码
	 */
	public String getInjuryIdentifyCerticode() {
		return injuryIdentifyCerticode;
	}

	/**
	 * @param injuryIdentifyCerticode 要设置的 伤残鉴定机构组织机构代码
	 */
	public void setInjuryIdentifyCerticode(String injuryIdentifyCerticode) {
		this.injuryIdentifyCerticode = injuryIdentifyCerticode;
	}

}
