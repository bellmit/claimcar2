/******************************************************************************
 * CREATETIME : 2016年6月1日 下午5:05:41
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHBIVClaimInjuryDataVo {

	@XmlElement(name = "InjuryPart")
	private String injurypart;// 受伤部位

	@XmlElement(name = "InjuryLevelCode")
	private String injuryLevelCode;  //伤残程度代码
	/**
	 * @return 返回 injurypart 受伤部位
	 */
	public String getInjurypart() {
		return injurypart;
	}

	/**
	 * @param injurypart 要设置的 受伤部位
	 */
	public void setInjurypart(String injurypart) {
		this.injurypart = injurypart;
	}

	public String getInjuryLevelCode() {
		return injuryLevelCode;
	}

	public void setInjuryLevelCode(String injuryLevelCode) {
		this.injuryLevelCode = injuryLevelCode;
	}

}
