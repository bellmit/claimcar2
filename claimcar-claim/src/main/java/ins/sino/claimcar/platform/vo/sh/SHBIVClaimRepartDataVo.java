/******************************************************************************
 * CREATETIME : 2016年6月1日 下午5:20:17
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHBIVClaimRepartDataVo {

	@XmlElement(name = "REPAIR_ORG", required = true)
	private String repairOrg;// 开票修理机构信息

	/**
	 * @return 返回 repairOrg 开票修理机构信息
	 */
	public String getRepairOrg() {
		return repairOrg;
	}

	/**
	 * @param repairOrg 要设置的 开票修理机构信息
	 */
	public void setRepairOrg(String repairOrg) {
		this.repairOrg = repairOrg;
	}

}
