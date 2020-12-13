package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 车辆损失部位
 * <pre></pre>
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CiLossCarLossPartDataVo {

	@XmlElement(name = "LOSS_PART", required = true)
	private String lossPart;// 车辆损失部位代码；参见代码

	/**
	 * @return 返回 lossPart 车辆损失部位代码；参见代码
	 */
	public String getLossPart() {
		return lossPart;
	}

	/**
	 * @param lossPart 要设置的 车辆损失部位代码；参见代码
	 */
	public void setLossPart(String lossPart) {
		this.lossPart = lossPart;
	}

}
