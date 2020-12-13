/******************************************************************************
 * CREATETIME : 2016年5月31日 下午6:54:59
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 车辆损失部位
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCIVClaimCarLossPartDataVo {

	@XmlElement(name = "LOSS_PART", required = true)
	private String lossPart;// 损失部位

	/**
	 * @return 返回 lossPart 损失部位
	 */
	public String getLossPart() {
		return lossPart;
	}

	/**
	 * @param lossPart 要设置的 损失部位
	 */
	public void setLossPart(String lossPart) {
		this.lossPart = lossPart;
	}

}
