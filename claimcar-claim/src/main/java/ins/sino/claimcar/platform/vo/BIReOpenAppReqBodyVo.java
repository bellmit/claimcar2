package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
public class BIReOpenAppReqBodyVo {

	/** 基本信息 **/
	@XmlElement(name = "BasePart")
	private BIReOpenAppReqBasePartVo basePartVo;

	public BIReOpenAppReqBasePartVo getBasePartVo() {
		return basePartVo;
	}

	public void setBasePartVo(BIReOpenAppReqBasePartVo basePartVo) {
		this.basePartVo = basePartVo;
	}

}
