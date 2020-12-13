/******************************************************************************
 * CREATETIME : 2016年6月6日 下午2:59:33
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * @author ★XMSH
 */
public class SHBIPaymentResBodyVo {

	@XmlElement(name = "BASE_PART")
	private SHBIPaymentResBasePartVo basePart;

	/**
	 * @return 返回 basePart。
	 */
	public SHBIPaymentResBasePartVo getBasePart() {
		return basePart;
	}

	/**
	 * @param basePart 要设置的 basePart。
	 */
	public void setBasePart(SHBIPaymentResBasePartVo basePart) {
		this.basePart = basePart;
	}

}
