/******************************************************************************
 * CREATETIME : 2016年6月6日 下午2:33:21
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author ★XMSH
 */
public class SHCIPaymentResBodyVo {

	@XmlElement(name = "BASE_PART")
	private SHCIPaymentResBasePartVo basePart;

	@XmlElement(name = "SUCCEED_GROUP")
	private SHCIPaymentResSucceedGroupVo succeedGroup;

	/**
	 * @return 返回 basePart。
	 */
	public SHCIPaymentResBasePartVo getBasePart() {
		return basePart;
	}

	/**
	 * @param basePart 要设置的 basePart。
	 */
	public void setBasePart(SHCIPaymentResBasePartVo basePart) {
		this.basePart = basePart;
	}

	/**
	 * @return 返回 succeedGroup。
	 */
	public SHCIPaymentResSucceedGroupVo getSucceedGroup() {
		return succeedGroup;
	}

	/**
	 * @param succeedGroup 要设置的 succeedGroup。
	 */
	public void setSucceedGroup(SHCIPaymentResSucceedGroupVo succeedGroup) {
		this.succeedGroup = succeedGroup;
	}
}
