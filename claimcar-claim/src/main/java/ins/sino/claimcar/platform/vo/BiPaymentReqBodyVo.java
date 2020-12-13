/******************************************************************************
 * CREATETIME : 2016年5月25日 下午2:47:05
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author ★XMSH
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Body")
public class BiPaymentReqBodyVo {

	@XmlElement(name = "BasePart")
	private BiPaymentBasePartVo basePart;

	@XmlElement(name = "PayData")
	private List<BiPaymentPayDataVo> payDataList;

	/**
	 * @return 返回 basePart。
	 */
	public BiPaymentBasePartVo getBasePart() {
		return basePart;
	}

	/**
	 * @param basePart 要设置的 basePart。
	 */
	public void setBasePart(BiPaymentBasePartVo basePart) {
		this.basePart = basePart;
	}

	/**
	 * @return 返回 payDataList。
	 */
	public List<BiPaymentPayDataVo> getPayDataList() {
		return payDataList;
	}

	/**
	 * @param payDataList 要设置的 payDataList。
	 */
	public void setPayDataList(List<BiPaymentPayDataVo> payDataList) {
		this.payDataList = payDataList;
	}

}
