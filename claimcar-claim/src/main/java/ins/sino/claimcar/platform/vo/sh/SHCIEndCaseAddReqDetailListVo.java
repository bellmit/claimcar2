/******************************************************************************
 * CREATETIME : 2016年6月1日 上午11:29:08
 ******************************************************************************/
package ins.sino.claimcar.platform.vo.sh;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 损失赔偿情况明细（多条）DETAIL_LIST（隶属于追加损失赔偿情况）
 * @author ★Luwei
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class SHCIEndCaseAddReqDetailListVo {

	@XmlElement(name = "CLAIM_FEE_TYPE")
	private String claimFeeType;// 损失赔偿类型

	@XmlElement(name = "DETAIL_FEE_TYPE")
	private String detailFeeType;// 损失赔偿类型细分

	@XmlElement(name = "LOSS_AMOUNT")
	private Double lossAmount;// 追加损失金额

	@XmlElement(name = "CLAIM_AMOUNT")
	private Double claimAmount;// 追加赔偿金额

	@XmlElement(name = "LIABILITY_RATE")
	private String liabilityRate;// 赔偿责任比例

	public String getClaimFeeType() {
		return claimFeeType;
	}

	public void setClaimFeeType(String claimFeeType) {
		this.claimFeeType = claimFeeType;
	}

	public String getDetailFeeType() {
		return detailFeeType;
	}

	public void setDetailFeeType(String detailFeeType) {
		this.detailFeeType = detailFeeType;
	}

	public Double getLossAmount() {
		return lossAmount;
	}

	public void setLossAmount(Double lossAmount) {
		this.lossAmount = lossAmount;
	}

	public Double getClaimAmount() {
		return claimAmount;
	}

	public void setClaimAmount(Double claimAmount) {
		this.claimAmount = claimAmount;
	}

	public String getLiabilityRate() {
		return liabilityRate;
	}

	public void setLiabilityRate(String liabilityRate) {
		this.liabilityRate = liabilityRate;
	}

}
