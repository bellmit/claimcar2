package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class BiEndCaseFraudTypeDataVo {
	@XmlElement(name = "FraudType", required = true)
	private String FraudType; //欺诈类型

	public String getFraudType() {
		return FraudType;
	}

	public void setFraudType(String fraudType) {
		FraudType = fraudType;
	}

}
