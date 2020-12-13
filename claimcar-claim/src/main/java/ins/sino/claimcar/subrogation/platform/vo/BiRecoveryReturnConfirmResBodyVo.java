package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 追偿回款确认接口 商业返回body
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
public class BiRecoveryReturnConfirmResBodyVo {

	@XmlElement(name = "BasePart")
	private BiRecoveryReturnConfirmDataVo recoveryConfirmDataVo;

	
	public BiRecoveryReturnConfirmDataVo getRecoveryConfirmDataVo() {
		return recoveryConfirmDataVo;
	}

	
	public void setRecoveryConfirmDataVo(BiRecoveryReturnConfirmDataVo recoveryConfirmDataVo) {
		this.recoveryConfirmDataVo = recoveryConfirmDataVo;
	}
	
	

	



	
}
