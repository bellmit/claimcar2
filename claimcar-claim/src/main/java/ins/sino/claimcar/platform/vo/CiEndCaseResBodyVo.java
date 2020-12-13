/******************************************************************************
 * CREATETIME : 2016年5月24日 下午3:34:00
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import java.util.List;

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
public class CiEndCaseResBodyVo {

	@XmlElement(name = "BASE_PART")
	private CiEndCaseResBasePartVo basePart;

	@XmlElementWrapper(name = "LOCKED_LIST")
	@XmlElement(name = "LOCKED_DATA")
	private List<CiEndCaseResLockedDataVo> lockedDataList;

	/**
	 * @return 返回 basePart。
	 */
	public CiEndCaseResBasePartVo getBasePart() {
		return basePart;
	}

	/**
	 * @param basePart 要设置的 basePart。
	 */
	public void setBasePart(CiEndCaseResBasePartVo basePart) {
		this.basePart = basePart;
	}

	/**
	 * @return 返回 lockedDataList。
	 */
	public List<CiEndCaseResLockedDataVo> getLockedDataList() {
		return lockedDataList;
	}

	/**
	 * @param lockedDataList 要设置的 lockedDataList。
	 */
	public void setLockedDataList(List<CiEndCaseResLockedDataVo> lockedDataList) {
		this.lockedDataList = lockedDataList;
	}

}
