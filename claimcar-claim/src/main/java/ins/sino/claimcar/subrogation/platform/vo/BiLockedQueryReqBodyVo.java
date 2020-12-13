/******************************************************************************
* CREATETIME : 2016年3月17日 下午7:16:59
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * 锁定查询接口 请求body 交强 
 * @author ★YangKun
 * @CreateTime 2016年3月17日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
public class BiLockedQueryReqBodyVo {

	@XmlElement(name = "BasePart")
	private BiLockedQueryBaseVo lockQueryBaseVo;

	public BiLockedQueryBaseVo getLockQueryBaseVo() {
		return lockQueryBaseVo;
	}

	
	public void setLockQueryBaseVo(BiLockedQueryBaseVo lockQueryBaseVo) {
		this.lockQueryBaseVo = lockQueryBaseVo;
	}
	
}
