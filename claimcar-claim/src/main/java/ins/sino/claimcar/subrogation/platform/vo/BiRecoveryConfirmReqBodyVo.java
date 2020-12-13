/******************************************************************************
* CREATETIME : 2016年3月17日 下午7:16:59
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * 开始追偿确认查询接口 商业请求body
 * @author ★YangKun
 * @CreateTime 2016年3月17日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class BiRecoveryConfirmReqBodyVo {

	@XmlElement(name = "BasePart")
	private BiRecoveryConfirmBaseVo biRecoveryConfirmBaseVo;

	public BiRecoveryConfirmBaseVo getBiRecoveryConfirmBaseVo() {
		return biRecoveryConfirmBaseVo;
	}

	public void setBiRecoveryConfirmBaseVo(
			BiRecoveryConfirmBaseVo biRecoveryConfirmBaseVo) {
		this.biRecoveryConfirmBaseVo = biRecoveryConfirmBaseVo;
	}

	

	
	
}
