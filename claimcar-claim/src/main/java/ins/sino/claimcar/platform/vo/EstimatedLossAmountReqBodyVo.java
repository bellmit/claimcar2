/******************************************************************************
 * CREATETIME : 2016年6月7日 下午3:01:23
 ******************************************************************************/
package ins.sino.claimcar.platform.vo;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author ★XMSH
 */
public class EstimatedLossAmountReqBodyVo {

	@XmlElement(name = "ESTIMATED_LOSS_AMOUNT_INFO")
	private EstimatedLossAmountInfoVo estimatedLossAmountInfo;

	/**
	 * @return 返回 estimatedLossAmountInfo。
	 */
	public EstimatedLossAmountInfoVo getEstimatedLossAmountInfo() {
		return estimatedLossAmountInfo;
	}

	/**
	 * @param estimatedLossAmountInfo 要设置的 estimatedLossAmountInfo。
	 */
	public void setEstimatedLossAmountInfo(EstimatedLossAmountInfoVo estimatedLossAmountInfo) {
		this.estimatedLossAmountInfo = estimatedLossAmountInfo;
	}

}
