/******************************************************************************
* CREATETIME : 2016年3月17日 下午7:16:59
******************************************************************************/
package ins.sino.claimcar.subrogation.platform.vo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author ★YangKun
 * @CreateTime 2016年3月17日
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
public class SubrogationCheckReqBodyVo {

	@XmlElement(name = "BasePart")
	private SubrogationCheckBaseVo subrogationCheckBaseVo;

	public SubrogationCheckBaseVo getSubrogationCheckBaseVo() {
		return subrogationCheckBaseVo;
	}

	public void setSubrogationCheckBaseVo(
			SubrogationCheckBaseVo subrogationCheckBaseVo) {
		this.subrogationCheckBaseVo = subrogationCheckBaseVo;
	}
	

	
}
