package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 重开赔案交强报文
 * 
 * @author Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class CIReOpenAppReqBodyVo {

	@XmlElement(name = "BASE_PART")
	private CIReOpenAppReqBasePartVo basePartVo;//

	/**
	 * @return 返回 basePartVo。
	 */
	public CIReOpenAppReqBasePartVo getBasePartVo() {
		return basePartVo;
	}

	/**
	 * @param basePartVo 要设置的 basePartVo。
	 */
	public void setBasePartVo(CIReOpenAppReqBasePartVo basePartVo) {
		this.basePartVo = basePartVo;
	}

}
