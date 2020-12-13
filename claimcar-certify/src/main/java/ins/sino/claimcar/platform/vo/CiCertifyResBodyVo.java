/******************************************************************************
 * CREATETIME : 2016年5月23日 下午3:15:43
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
public class CiCertifyResBodyVo {

	@XmlElementWrapper(name = "LOCKED_LIST")
	@XmlElement(name = "LOCKED_DATA")
	private List<CiCertifyResLockedDataVo> lockDatas;

	/**
	 * @return 返回 lockDatas。
	 */
	public List<CiCertifyResLockedDataVo> getLockDatas() {
		return lockDatas;
	}

	/**
	 * @param lockDatas 要设置的 lockDatas。
	 */
	public void setLockDatas(List<CiCertifyResLockedDataVo> lockDatas) {
		this.lockDatas = lockDatas;
	}

}
