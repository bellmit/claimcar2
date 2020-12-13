/******************************************************************************
 * CREATETIME : 2016年4月27日 下午4:49:32
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 车险信息平台交强险V6.0.0接口 --> 查勘登记vo --> 交强-->车辆查勘情况列表vo
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class CICheckReqCarListVo {

	/** 车辆损失情况列表 **/
	@XmlElement(name = "CAR_DATA")
	private List<CICheckReqCarDataVo> carDataVo;

	
	
	public List<CICheckReqCarDataVo> getCarDataVo() {
		return carDataVo;
	}

	public void setCarDataVo(List<CICheckReqCarDataVo> carDataVo) {
		this.carDataVo = carDataVo;
	}

}
