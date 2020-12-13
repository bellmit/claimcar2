package ins.sino.claimcar.subrogationsh.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 上海商业发送代位求偿信息抄回请求Body
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class SubrogationSHCopyInformationBodyVo {

	@XmlElement(name = "BasePart")
	private SubrogationSHCopyInformationBasePartVo basePartVo;

	public SubrogationSHCopyInformationBasePartVo getBasePartVo() {
		return basePartVo;
	}

	public void setBasePartVo(SubrogationSHCopyInformationBasePartVo basePartVo) {
		this.basePartVo = basePartVo;
	}

}
