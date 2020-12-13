/******************************************************************************
 * CREATETIME : 2016年6月1日 下午5:13:21
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHBIVClaimCarLossPartDataVo {

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
