/******************************************************************************
 * CREATETIME : 2016年5月26日 下午4:51:24
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 承修修理机构（多条）ActuralRepairList（隶属于车辆损失情况）
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCILossReqActuralRepairDataVo {

	/**
	 * 承修修理机构信息
	 */
	@XmlElement(name = "ActuralRepairOrg")
	private String acturalRepairOrg;//承修修理机构信息

	public String getActuralRepairOrg() {
		return acturalRepairOrg;
	}

	public void setActuralRepairOrg(String acturalRepairOrg) {
		this.acturalRepairOrg = acturalRepairOrg;
	}

}
