/******************************************************************************
 * CREATETIME : 2016年6月1日 下午3:09:00
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
public class SHCIEndCaseAddReqRepairDataVo {

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
