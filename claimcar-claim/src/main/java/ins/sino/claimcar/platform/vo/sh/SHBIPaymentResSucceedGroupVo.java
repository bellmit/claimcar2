/******************************************************************************
 * CREATETIME : 2016年6月6日 下午3:00:05
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * @author ★XMSH
 */
public class SHBIPaymentResSucceedGroupVo {

	@XmlElement(name = "CIRC_PAY_NO")
	private String circPayNo;// 平台赔款支付码

	@XmlElement(name = "CIRC_SUCCEED_AMOUNT")
	private Double circSucceedAmount;// 平台支付金额

	@XmlElementWrapper(name = "PAY_SUCCEED_LIST")
	@XmlElement(name = "PAY_SUCCEED_DATA")
	private List<SHBIPaymentResPaySucceedDataVo> paySucceedList;

	/**
	 * @return 返回 circPayNo 平台赔款支付码
	 */
	public String getCircPayNo() {
		return circPayNo;
	}

	/**
	 * @param circPayNo 要设置的 平台赔款支付码
	 */
	public void setCircPayNo(String circPayNo) {
		this.circPayNo = circPayNo;
	}

	/**
	 * @return 返回 circSucceedAmount 平台支付金额
	 */
	public Double getCircSucceedAmount() {
		return circSucceedAmount;
	}

	/**
	 * @param circSucceedAmount 要设置的 平台支付金额
	 */
	public void setCircSucceedAmount(Double circSucceedAmount) {
		this.circSucceedAmount = circSucceedAmount;
	}

	/**
	 * @return 返回 paySucceedList。
	 */
	public List<SHBIPaymentResPaySucceedDataVo> getPaySucceedList() {
		return paySucceedList;
	}

	/**
	 * @param paySucceedList 要设置的 paySucceedList。
	 */
	public void setPaySucceedList(List<SHBIPaymentResPaySucceedDataVo> paySucceedList) {
		this.paySucceedList = paySucceedList;
	}

}
