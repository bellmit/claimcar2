/******************************************************************************
 * CREATETIME : 2016年5月23日 下午4:28:56
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author ★XMSH
 */
public class BiCertifyResBodyVo {

	@XmlElement(name = "LockedData")
	private List<BiCertifyResLockedDataVo> lockedDatas;

	/**
	 * @return 返回 lockedDatas。
	 */
	public List<BiCertifyResLockedDataVo> getLockedDatas() {
		return lockedDatas;
	}

	/**
	 * @param lockedDatas 要设置的 lockedDatas。
	 */
	public void setLockedDatas(List<BiCertifyResLockedDataVo> lockedDatas) {
		this.lockedDatas = lockedDatas;
	}

}
