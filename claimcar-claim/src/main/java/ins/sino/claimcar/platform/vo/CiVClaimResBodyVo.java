/******************************************************************************
 * CREATETIME : 2016年5月23日 下午5:38:13
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
public class CiVClaimResBodyVo {

	@XmlElementWrapper(name = "LOCKED_LIST")
	@XmlElement(name = "LOCKED_DATA")
	private List<CiVClaimResLockedDataVo> lockedDataList;

	/**
	 * @return 返回 lockedDataList。
	 */
	public List<CiVClaimResLockedDataVo> getLockedDataList() {
		return lockedDataList;
	}

	/**
	 * @param lockedDataList 要设置的 lockedDataList。
	 */
	public void setLockedDataList(List<CiVClaimResLockedDataVo> lockedDataList) {
		this.lockedDataList = lockedDataList;
	}

}
