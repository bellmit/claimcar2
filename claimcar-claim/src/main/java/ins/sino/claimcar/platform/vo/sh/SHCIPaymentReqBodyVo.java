/******************************************************************************
 * CREATETIME : 2016年6月6日 下午2:28:13
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 上海赔款支付
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class SHCIPaymentReqBodyVo {

	@XmlElement(name = "BASE_PART")
	private SHCIPaymentBasePartVo basePart;

	@XmlElementWrapper(name = "PAY_LIST")
	@XmlElement(name = "PAY_DATA")
	private List<SHCIPaymentPayDataVo> payList;

	/**
	 * @return 返回 basePart。
	 */
	public SHCIPaymentBasePartVo getBasePart() {
		return basePart;
	}

	/**
	 * @param basePart 要设置的 basePart。
	 */
	public void setBasePart(SHCIPaymentBasePartVo basePart) {
		this.basePart = basePart;
	}

	/**
	 * @return 返回 payList。
	 */
	public List<SHCIPaymentPayDataVo> getPayList() {
		return payList;
	}

	/**
	 * @param payList 要设置的 payList。
	 */
	public void setPayList(List<SHCIPaymentPayDataVo> payList) {
		this.payList = payList;
	}
}
