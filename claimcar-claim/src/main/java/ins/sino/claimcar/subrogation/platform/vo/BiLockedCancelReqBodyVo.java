/******************************************************************************
* CREATETIME : 2016年3月30日 上午11:44:44
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * 锁定取消请求body
 * @author ★YangKun
 * @CreateTime 2016年3月30日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
public class BiLockedCancelReqBodyVo {
	
	@XmlElement(name = "BasePart")
	private BiLockedCancelBaseVo lockCancelBaseVo;

	
	public BiLockedCancelBaseVo getLockCancelBaseVo() {
		return lockCancelBaseVo;
	}

	
	public void setLockCancelBaseVo(BiLockedCancelBaseVo lockCancelBaseVo) {
		this.lockCancelBaseVo = lockCancelBaseVo;
	}

	
	


}
