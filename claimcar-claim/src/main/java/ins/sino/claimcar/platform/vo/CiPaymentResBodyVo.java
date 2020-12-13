/******************************************************************************
 * CREATETIME : 2016年5月25日 下午2:35:56
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
public class CiPaymentResBodyVo {

	@XmlElementWrapper(name = "LOCKED_LIST")
	@XmlElement(name = "LOCKED_DATA")
	private List<CiPaymentResLockedDataVo> lockedDataList;

	/**
	 * @return 返回 lockedDataList。
	 */
	public List<CiPaymentResLockedDataVo> getLockedDataList() {
		return lockedDataList;
	}

	/**
	 * @param lockedDataList 要设置的 lockedDataList。
	 */
	public void setLockedDataList(List<CiPaymentResLockedDataVo> lockedDataList) {
		this.lockedDataList = lockedDataList;
	}

}
