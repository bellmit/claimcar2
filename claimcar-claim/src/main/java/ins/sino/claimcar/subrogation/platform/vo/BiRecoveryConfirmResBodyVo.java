package ins.sino.claimcar.subrogation.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 开始追偿确认查询接口 商业返回body
 * @author ★YangKun
 * @CreateTime 2016年3月16日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
public class BiRecoveryConfirmResBodyVo {

	@XmlElement(name = "BasePart")
	private List<BiRecoveryConfirmDataVo> biRecoveryConfirmDataVos;

	public List<BiRecoveryConfirmDataVo> getBiRecoveryConfirmDataVos() {
		return biRecoveryConfirmDataVos;
	}

	public void setBiRecoveryConfirmDataVos(
			List<BiRecoveryConfirmDataVo> biRecoveryConfirmDataVos) {
		this.biRecoveryConfirmDataVos = biRecoveryConfirmDataVos;
	}



	
}
