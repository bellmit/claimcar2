/******************************************************************************
 * CREATETIME : 2016年5月26日 下午4:28:12
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 车辆损失部位（多条）LOSS_PART_LIST（隶属于车辆损失情况）
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCILossReqCarLossPartDataVo {

	/**
	 * 损失部位
	 */
	@XmlElement(name = "LOSS_PART")
	private String lossPart;

	
	
	public String getLossPart() {
		return lossPart;
	}

	public void setLossPart(String lossPart) {
		this.lossPart = lossPart;
	}

}
