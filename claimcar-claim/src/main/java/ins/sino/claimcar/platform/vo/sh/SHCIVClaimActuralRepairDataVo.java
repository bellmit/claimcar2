/******************************************************************************
 * CREATETIME : 2016年5月31日 下午7:07:59
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 承修修理机构
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCIVClaimActuralRepairDataVo {

	@XmlElement(name = "ActuralRepairOrg", required = true)
	private String acturalrepairorg;// 承修修理机构信息

	/**
	 * @return 返回 acturalrepairorg 承修修理机构信息
	 */
	public String getActuralrepairorg() {
		return acturalrepairorg;
	}

	/**
	 * @param acturalrepairorg 要设置的 承修修理机构信息
	 */
	public void setActuralrepairorg(String acturalrepairorg) {
		this.acturalrepairorg = acturalrepairorg;
	}

}
