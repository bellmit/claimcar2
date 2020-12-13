/******************************************************************************
 * CREATETIME : 2016年5月26日 下午5:49:31
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 渠道信息（多条）SALES_CHANNLE_CODE_LIST
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCILossResSalesChannleDataVo {

	/**
	 * 修理机构信息
	 */
	@XmlElement(name = "REPAIR_ORG")
	private String repairOrg;// 修理机构信息

	/**
	 * 等级
	 */
	@XmlElement(name = "LEVEL_TYPE")
	private String levelType;// 等级

	public String getRepairOrg() {
		return repairOrg;
	}

	public void setRepairOrg(String repairOrg) {
		this.repairOrg = repairOrg;
	}

	public String getLevelType() {
		return levelType;
	}

	public void setLevelType(String levelType) {
		this.levelType = levelType;
	}
}
