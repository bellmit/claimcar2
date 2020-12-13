package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class CiLossPersonInjuryDataVo {

	@XmlElement(name = "INJURY_PART")
	private String injuryPart;// 受伤部位

	@XmlElement(name = "INJURY_LEVEL_CODE")
	private String injuryLevelCode;// 伤残程度代码；参见代码

	/**
	 * @return 返回 injuryPart 受伤部位
	 */
	public String getInjuryPart() {
		return injuryPart;
	}

	/**
	 * @param injuryPart 要设置的 受伤部位
	 */
	public void setInjuryPart(String injuryPart) {
		this.injuryPart = injuryPart;
	}

	/**
	 * @return 返回 injuryLevelCode 伤残程度代码；参见代码
	 */
	public String getInjuryLevelCode() {
		return injuryLevelCode;
	}

	/**
	 * @param injuryLevelCode 要设置的 伤残程度代码；参见代码
	 */
	public void setInjuryLevelCode(String injuryLevelCode) {
		this.injuryLevelCode = injuryLevelCode;
	}

}
