/******************************************************************************
 * CREATETIME : 2016年4月27日 下午4:58:21
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 车险信息平台交强险V6.0.0接口 --> 查勘登记vo --> 交强-->人员查勘情况列表vo
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CICheckReqPersonListVo {

	/** 人员损失情况列表 **/
	@XmlElement(name = "PERSON_DATA")
	private List<CICheckReqPersonDataVo> personDataVo;

	
	
	public List<CICheckReqPersonDataVo> getPersonDataVo() {
		return personDataVo;
	}

	public void setPersonDataVo(List<CICheckReqPersonDataVo> personDataVo) {
		this.personDataVo = personDataVo;
	}

}
