package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class BiLossCarLossPartDataVo {

	@XmlElement(name = "LossPart")
	private String lossPart;// 2002

	/**
	 * @return 返回 lossPart 2002
	 */
	public String getLossPart() {
		return lossPart;
	}

	/**
	 * @param lossPart 要设置的 2002
	 */
	public void setLossPart(String lossPart) {
		this.lossPart = lossPart;
	}

}
