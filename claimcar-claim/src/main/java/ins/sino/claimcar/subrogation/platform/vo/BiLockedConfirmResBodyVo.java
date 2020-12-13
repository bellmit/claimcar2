/******************************************************************************
* CREATETIME : 2016年3月30日 上午11:50:43
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * 锁定确认返回 body
 * @author ★YangKun
 * @CreateTime 2016年3月30日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
public class BiLockedConfirmResBodyVo {

	@XmlElement(name = "BasePart")
	private BiLockedConfirmResBaseVo lockConfirmBaseVo;

	
	public BiLockedConfirmResBaseVo getLockConfirmBaseVo() {
		return lockConfirmBaseVo;
	}

	
	public void setLockConfirmBaseVo(BiLockedConfirmResBaseVo lockConfirmBaseVo) {
		this.lockConfirmBaseVo = lockConfirmBaseVo;
	}
	
	
	
}
