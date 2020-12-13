/******************************************************************************
 * CREATETIME : 2016年4月27日 下午4:54:30
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 车险信息平台交强险V6.0.0接口 --> 查勘登记vo --> 交强-->财产查勘情况列表vo
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CICheckReqProtectListVo {

	/** 财产损失情况列表 **/
	@XmlElement(name = "PROTECT_DATA")
	private List<CICheckReqProtectDataVo> protectDataVo;

	
	
	public List<CICheckReqProtectDataVo> getProtectDataVo() {
		return protectDataVo;
	}

	public void setProtectDataVo(List<CICheckReqProtectDataVo> protectDataVo) {
		this.protectDataVo = protectDataVo;
	}

}
