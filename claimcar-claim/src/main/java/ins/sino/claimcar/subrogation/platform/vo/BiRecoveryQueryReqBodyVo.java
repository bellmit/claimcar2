/******************************************************************************
* CREATETIME : 2016年3月17日 下午7:16:59
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * 结算查询 请求商业 body
 * @author ★YangKun
 * @CreateTime 2016年3月17日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class BiRecoveryQueryReqBodyVo {

	@XmlElement(name = "BasePart")
	private BiRecoveryQueryBaseVo recoveryQueryBaseVo;

	
	public BiRecoveryQueryBaseVo getRecoveryQueryBaseVo() {
		return recoveryQueryBaseVo;
	}

	
	public void setRecoveryQueryBaseVo(BiRecoveryQueryBaseVo recoveryQueryBaseVo) {
		this.recoveryQueryBaseVo = recoveryQueryBaseVo;
	}
	
}
