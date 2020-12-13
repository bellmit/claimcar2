/******************************************************************************
 * CREATETIME : 2016年6月6日 下午2:50:41
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "BODY")
public class SHBIPaymentReqBodyVo {

	@XmlElement(name = "BASE_PART")
	private SHBIPaymentBasePartVo basePart;

	@XmlElementWrapper(name = "PAY_LIST")
	@XmlElement(name = "PAY_DATA")
	private List<SHBIPaymentPayDataVo> payList;

	/**
	 * @return 返回 basePart。
	 */
	public SHBIPaymentBasePartVo getBasePart() {
		return basePart;
	}

	/**
	 * @param basePart 要设置的 basePart。
	 */
	public void setBasePart(SHBIPaymentBasePartVo basePart) {
		this.basePart = basePart;
	}

	/**
	 * @return 返回 payList。
	 */
	public List<SHBIPaymentPayDataVo> getPayList() {
		return payList;
	}

	/**
	 * @param payList 要设置的 payList。
	 */
	public void setPayList(List<SHBIPaymentPayDataVo> payList) {
		this.payList = payList;
	}

}
