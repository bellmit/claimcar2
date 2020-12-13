/******************************************************************************
 * CREATETIME : 2016年5月23日 上午11:37:44
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
public class BiClaimResBodyVo {

	@XmlElement(name = "BasePart")
	private BiClaimResBasePartVo basePart;

	@XmlElement(name = "LockedData")
	private BiClaimResLockedDataVo lockedData;

	/**
	 * @return 返回 basePart。
	 */
	public BiClaimResBasePartVo getBasePart() {
		return basePart;
	}

	/**
	 * @param basePart 要设置的 basePart。
	 */
	public void setBasePart(BiClaimResBasePartVo basePart) {
		this.basePart = basePart;
	}

	/**
	 * @return 返回 lockedData。
	 */
	public BiClaimResLockedDataVo getLockedData() {
		return lockedData;
	}

	/**
	 * @param lockedData 要设置的 lockedData。
	 */
	public void setLockedData(BiClaimResLockedDataVo lockedData) {
		this.lockedData = lockedData;
	}

}
