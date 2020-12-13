/******************************************************************************
 * CREATETIME : 2016年5月25日 下午2:27:16
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

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
public class CiPaymentReqBodyVo {

	@XmlElement(name = "BASE_PART")
	private CiPaymentBasePartVo basePart;

	@XmlElementWrapper(name = "PAY_LIST")
	@XmlElement(name = "PAY_DATA")
	private List<CiPaymentPayDataVo> payDataList;

	/**
	 * @return 返回 basePart。
	 */
	public CiPaymentBasePartVo getBasePart() {
		return basePart;
	}

	/**
	 * @param basePart 要设置的 basePart。
	 */
	public void setBasePart(CiPaymentBasePartVo basePart) {
		this.basePart = basePart;
	}

	/**
	 * @return 返回 payDataList。
	 */
	public List<CiPaymentPayDataVo> getPayDataList() {
		return payDataList;
	}

	/**
	 * @param payDataList 要设置的 payDataList。
	 */
	public void setPayDataList(List<CiPaymentPayDataVo> payDataList) {
		this.payDataList = payDataList;
	}

}
