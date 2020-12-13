/******************************************************************************
 * CREATETIME : 2016年6月1日 上午10:34:18
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 人员受伤部位（多条）（隶属于人员损失情况）
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCIEndCaseAddReqInjuryDataVo {

	@XmlElement(name = "InjuryPart")
	private String injuryPart;// 受伤部位
	
	/* 牛强 2017-03-15 改 */
	@XmlElement(name = "InjuryLevelCode")
	private String InjuryLevelCode;// 伤残程度代码

	public String getInjuryPart() {
		return injuryPart;
	}

	public void setInjuryPart(String injuryPart) {
		this.injuryPart = injuryPart;
	}

	public String getInjuryLevelCode() {
		return InjuryLevelCode;
	}

	public void setInjuryLevelCode(String injuryLevelCode) {
		InjuryLevelCode = injuryLevelCode;
	}
}
