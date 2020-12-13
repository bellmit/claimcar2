/******************************************************************************
 * CREATETIME : 2016年6月1日 下午7:41:46
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author ★XMSH
 */
public class SHBIVClaimResSalesChannleCodeDataVo {

	@XmlElement(name = "REPAIR_ORG", required = true)
	private String repairOrg;// 修理机构信息

	@XmlElement(name = "LEVEL_TYPE", required = true)
	private String levelType;// 等级

	/**
	 * @return 返回 repairOrg 修理机构信息
	 */
	public String getRepairOrg() {
		return repairOrg;
	}

	/**
	 * @param repairOrg 要设置的 修理机构信息
	 */
	public void setRepairOrg(String repairOrg) {
		this.repairOrg = repairOrg;
	}

	/**
	 * @return 返回 levelType 等级
	 */
	public String getLevelType() {
		return levelType;
	}

	/**
	 * @param levelType 要设置的 等级
	 */
	public void setLevelType(String levelType) {
		this.levelType = levelType;
	}

}
