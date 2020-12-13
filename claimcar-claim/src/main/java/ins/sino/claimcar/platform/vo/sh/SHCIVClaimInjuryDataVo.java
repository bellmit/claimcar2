/******************************************************************************
 * CREATETIME : 2016年6月1日 上午10:51:39
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 人员受伤部位
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCIVClaimInjuryDataVo {

	@XmlElement(name = "InjuryPart")
	private String injurypart;// 受伤部位
	
	/*牛强  2017-03-15 改*/
	@XmlElement(name="InjuryLevelCode")
	private String InjuryLevelCode;//伤残程度代码

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
		return InjuryLevelCode;
	}

	public void setInjuryLevelCode(String injuryLevelCode) {
		InjuryLevelCode = injuryLevelCode;
	}

}
