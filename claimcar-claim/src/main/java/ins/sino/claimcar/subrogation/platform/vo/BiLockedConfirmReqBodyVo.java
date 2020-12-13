/******************************************************************************
* CREATETIME : 2016年3月30日 上午11:44:44
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * 锁定确认请求body
 * @author ★YangKun
 * @CreateTime 2016年3月30日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
public class BiLockedConfirmReqBodyVo {
	
	@XmlElement(name = "BasePart")
	private BiLockedConfirmBaseVo lockConfirmBaseVo;

	
	public BiLockedConfirmBaseVo getLockConfirmBaseVo() {
		return lockConfirmBaseVo;
	}

	
	public void setLockConfirmBaseVo(BiLockedConfirmBaseVo lockConfirmBaseVo) {
		this.lockConfirmBaseVo = lockConfirmBaseVo;
	}



}
