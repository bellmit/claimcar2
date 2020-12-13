/******************************************************************************
 * CREATETIME : 2016年5月26日 下午4:47:46
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 开票修理机构（多条）REPAIR_LIST（隶属于车辆损失情况）
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCILossReqRepairDataVo {

	/**
	 * 开票修理机构信息
	 */
	@XmlElement(name = "REPAIR_ORG")
	private String repairOrg;// 开票修理机构信息

	
	public String getRepairOrg() {
		return repairOrg;
	}

	public void setRepairOrg(String repairOrg) {
		this.repairOrg = repairOrg;
	}

}
