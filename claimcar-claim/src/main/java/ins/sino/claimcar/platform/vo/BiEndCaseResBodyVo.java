/******************************************************************************
 * CREATETIME : 2016年5月24日 下午5:23:45
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
public class BiEndCaseResBodyVo {

	@XmlElement(name = "BasePart")
	private BiEndCaseResBasePartVo basePart;

	/**
	 * @return 返回 basePart。
	 */
	public BiEndCaseResBasePartVo getBasePart() {
		return basePart;
	}

	/**
	 * @param basePart 要设置的 basePart。
	 */
	public void setBasePart(BiEndCaseResBasePartVo basePart) {
		this.basePart = basePart;
	}

	/**
	 * @return 返回 lockedDataList。
	 */
	public List<BiEndCaseResLockedDataVo> getLockedDataList() {
		return lockedDataList;
	}

	/**
	 * @param lockedDataList 要设置的 lockedDataList。
	 */
	public void setLockedDataList(List<BiEndCaseResLockedDataVo> lockedDataList) {
		this.lockedDataList = lockedDataList;
	}

	@XmlElement(name = "LockedData")
	private List<BiEndCaseResLockedDataVo> lockedDataList;
}
