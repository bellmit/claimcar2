/******************************************************************************
 * CREATETIME : 2016年5月23日 上午9:29:41
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class CiClaimReqBodyVo {

	@XmlElement(name = "BASE_PART")
	private CiClaimBasePartVo basePart;

	/**
	 * @return 返回 basePart。
	 */
	public CiClaimBasePartVo getBasePart() {
		return basePart;
	}

	/**
	 * @param basePart 要设置的 basePart。
	 */
	public void setBasePart(CiClaimBasePartVo basePart) {
		this.basePart = basePart;
	}

}
