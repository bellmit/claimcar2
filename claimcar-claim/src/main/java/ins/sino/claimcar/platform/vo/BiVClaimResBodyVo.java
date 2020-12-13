/******************************************************************************
 * CREATETIME : 2016年5月24日 上午11:19:10
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
@XmlRootElement(name = "BODY")
public class BiVClaimResBodyVo {

	@XmlElement(name = "LockedData")
	private List<BiVClaimResLockedDataVo> lockedDataList;

	/**
	 * @return 返回 lockedDataList。
	 */
	public List<BiVClaimResLockedDataVo> getLockedDataList() {
		return lockedDataList;
	}

	/**
	 * @param lockedDataList 要设置的 lockedDataList。
	 */
	public void setLockedDataList(List<BiVClaimResLockedDataVo> lockedDataList) {
		this.lockedDataList = lockedDataList;
	}
}
