/******************************************************************************
 * CREATETIME : 2016年6月7日 下午3:01:23
 ******************************************************************************/
package ins.sino.claimcar.claim.vo;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * @author ★XMSH
 */
public class EstimatedLossAmountReqBodyVo {

	@XmlElement(name = "ESTIMATED_LOSS_AMOUNT_INFO")
	private List<EstimatedLossAmountInfoVo> estimatedLossAmountInfo;

	
	public List<EstimatedLossAmountInfoVo> getEstimatedLossAmountInfo() {
		return estimatedLossAmountInfo;
	}

	
	public void setEstimatedLossAmountInfo(List<EstimatedLossAmountInfoVo> estimatedLossAmountInfo) {
		this.estimatedLossAmountInfo = estimatedLossAmountInfo;
	}


}
