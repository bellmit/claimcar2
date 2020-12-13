/******************************************************************************
 * CREATETIME : 2016年5月26日 下午3:27:52
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
public class SHCILossReqInjuryDataVo {

	@XmlElement(name = "InjuryPart")
	private String injuryPart;// 受伤部位

	
	@XmlElement(name = "InjuryLevelCode")
	private String injuryLevelCode;// 伤残程度代码
	
	
	public String getInjuryLevelCode() {
		return injuryLevelCode;
	}

	public void setInjuryLevelCode(String injuryLevelCode) {
		this.injuryLevelCode = injuryLevelCode;
	}

	public String getInjuryPart() {
		return injuryPart;
	}

	public void setInjuryPart(String injuryPart) {
		this.injuryPart = injuryPart;
	}

}
