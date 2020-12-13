/******************************************************************************
 * CREATETIME : 2016年5月23日 上午10:33:40
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class CiClaimResBodyVo {

	@XmlElement(name = "BASE_PART")
	private CiClaimResBasePartVo basePart;

	@XmlElementWrapper(name = "LOCKED_LIST")
	@XmlElement(name = "LOCKED_DATA")
	private CiClaimResLockedDataVo lockedData;

	/**
	 * @return 返回 basePart。
	 */
	public CiClaimResBasePartVo getBasePart() {
		return basePart;
	}

	/**
	 * @param basePart 要设置的 basePart。
	 */
	public void setBasePart(CiClaimResBasePartVo basePart) {
		this.basePart = basePart;
	}

	/**
	 * @return 返回 lockedData。
	 */
	public CiClaimResLockedDataVo getLockedData() {
		return lockedData;
	}

	/**
	 * @param lockedData 要设置的 lockedData。
	 */
	public void setLockedData(CiClaimResLockedDataVo lockedData) {
		this.lockedData = lockedData;
	}
}
